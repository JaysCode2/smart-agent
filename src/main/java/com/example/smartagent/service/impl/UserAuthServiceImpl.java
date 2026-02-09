package com.example.smartagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.smartagent.dto.LoginRequest;
import com.example.smartagent.dto.LoginResponse;
import com.example.smartagent.dto.RegisterRequest;
import com.example.smartagent.entity.LoginLog;
import com.example.smartagent.entity.UserInfo;
import com.example.smartagent.mapper.LoginLogRepository;
import com.example.smartagent.mapper.UserInfoMapper;
import com.example.smartagent.service.UserAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * 用户认证服务实现
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private static final Logger log = Logger.getLogger(UserAuthServiceImpl.class.getName());

    private final UserInfoMapper userInfoMapper;
    private final LoginLogRepository loginLogRepository;
    private final PasswordEncoder passwordEncoder;

    // 登入失败最大次数
    private static final int MAX_LOGIN_FAILURES = 5;
    // 账号锁定时长（分钟）
    private static final int LOCK_DURATION_MINUTES = 15;

    public UserAuthServiceImpl(UserInfoMapper userInfoMapper, LoginLogRepository loginLogRepository,
                             PasswordEncoder passwordEncoder) {
        this.userInfoMapper = userInfoMapper;
        this.loginLogRepository = loginLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfo register(RegisterRequest request) {
        // ==================== 验证 ====================
        // 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        if (usernameExists(request.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (emailExists(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已被注册");
        }

        // 检查手机号是否已存在
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            UserInfo phoneExists = userInfoMapper.selectByPhone(request.getPhone());
            if (phoneExists != null) {
                throw new IllegalArgumentException("手机号已被注册");
            }
        }

        // ==================== 创建用户 ====================
        UserInfo userInfo = UserInfo.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(encodePassword(request.getPassword()))
                .userStatus(1) // 1-正常
                .loginFailureCount(0)
                .isDeleted(0)
                .version(1)
                .build();

        // 保存用户到数据库
        int result = userInfoMapper.insert(userInfo);
        if (result <= 0) {
            throw new RuntimeException("用户注册失败，请稍后重试");
        }

        log.info("用户注册成功: username=" + request.getUsername());
        return userInfo;
    }

    @Override
    public LoginResponse login(LoginRequest request, String clientIp) {
        // ==================== 基础验证 ====================
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalArgumentException("密码不能为空或长度不足");
        }

        // ==================== 查询用户 ====================
        UserInfo userInfo = userInfoMapper.selectByIdentifier(request.getUsername());
        if (userInfo == null) {
            recordLoginFailure(request.getUsername(), clientIp, "用户不存在");
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // ==================== 防御逻辑 ====================
        // 1. 检查账号是否被锁定
        if (isAccountLocked(userInfo.getId())) {
            recordLoginFailure(request.getUsername(), clientIp, "账号已被锁定");
            throw new IllegalArgumentException("账号已被锁定，请在15分钟后再试");
        }

        // 2. 检查账号状态
        if (userInfo.getUserStatus() == 0) {
            recordLoginFailure(request.getUsername(), clientIp, "账号已禁用");
            throw new IllegalArgumentException("账号已被禁用");
        }

        if (userInfo.getUserStatus() == 2) {
            recordLoginFailure(request.getUsername(), clientIp, "账号被锁定");
            throw new IllegalArgumentException("账号已被锁定");
        }

        // 3. 验证密码
        if (!verifyPassword(request.getPassword(), userInfo.getPasswordHash())) {
            // 登入失败次数+1
            userInfo.setLoginFailureCount(userInfo.getLoginFailureCount() + 1);

            // 如果失败次数超过限制，锁定账号
            if (userInfo.getLoginFailureCount() >= MAX_LOGIN_FAILURES) {
                userInfo.setUserStatus(2); // 2-锁定
                userInfo.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                recordLoginFailure(request.getUsername(), clientIp, "登入失败次数过多，账号已锁定");
            } else {
                recordLoginFailure(request.getUsername(), clientIp, "密码错误");
            }

            userInfoMapper.updateById(userInfo);
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // ==================== 登入成功 ====================
        // 1. 重置失败次数
        userInfo.setLoginFailureCount(0);
        userInfo.setUserStatus(1);
        userInfo.setLockedUntil(null);

        // 2. 更新最后登入时间和IP
        userInfo.setLastLoginTime(LocalDateTime.now());
        userInfo.setLastLoginIp(clientIp);

        // 3. 保存到数据库
        log.info("userID:"+userInfo.getId());
        userInfoMapper.updateById(userInfo);

        // 4. 记录登入日志
        LoginLog loginLog = LoginLog.builder()
                .userId(userInfo.getId())
                .username(userInfo.getUsername())
                .loginIp(clientIp)
                .loginTime(LocalDateTime.now())
                .loginResult(1) // 1-成功
                .build();
        loginLogRepository.save(loginLog);

        log.info("用户登入成功: userId=" + userInfo.getId() + ", username=" + userInfo.getUsername() + ", ip=" + clientIp);

        // 5. 返回登入响应
        return LoginResponse.builder()
                .userId(userInfo.getId())
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .realName(userInfo.getRealName())
                .avatarUrl(userInfo.getAvatarUrl())
                .lastLoginTime(userInfo.getLastLoginTime())
                .loginTime(LocalDateTime.now())
                .build();
    }

    @Override
    public void logout(Long userId) {
        if (userId != null && userId > 0) {
            log.info("用户登出: userId=" + userId);
        }
    }

    @Override
    public UserInfo getCurrentUser() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                Object userId = attributes.getRequest().getSession().getAttribute("userId");
                if (userId instanceof Long) {
                    return userInfoMapper.selectById((Long) userId);
                }
            }
        } catch (Exception e) {
            log.log(java.util.logging.Level.WARNING, "获取当前用户失败", e);
        }
        return null;
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "密码验证失败", e);
            return false;
        }
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean usernameExists(String username) {
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        return userInfo != null;
    }

    @Override
    public boolean emailExists(String email) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        return userInfo != null;
    }

    @Override
    public boolean isAccountLocked(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            return false;
        }

        // 检查是否处于锁定期间
        if (userInfo.getLockedUntil() != null && LocalDateTime.now().isBefore(userInfo.getLockedUntil())) {
            return true;
        }

        // 检查登入失败次数
        if (userInfo.getLoginFailureCount() >= MAX_LOGIN_FAILURES) {
            return true;
        }

        return false;
    }

    @Override
    public void recordLoginFailure(String identifier, String clientIp, String reason) {
        try {
            LoginLog loginLog = LoginLog.builder()
                    .username(identifier)
                    .loginIp(clientIp)
                    .loginTime(LocalDateTime.now())
                    .loginResult(0) // 0-失败
                    .failureReason(reason)
                    .build();
            loginLogRepository.save(loginLog);
            log.warning("记录登入失败: identifier=" + identifier + ", ip=" + clientIp + ", reason=" + reason);
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "记录登入失败日志异常", e);
        }
    }

    @Override
    public void resetLoginFailureCount(Long userId) {
        if (userId != null && userId > 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);
            userInfo.setLoginFailureCount(0);
            userInfoMapper.updateById(userInfo);
        }
    }
}
