package com.example.smartagent.controller;

import com.example.smartagent.dto.ApiResponse;
import com.example.smartagent.dto.LoginRequest;
import com.example.smartagent.dto.LoginResponse;
import com.example.smartagent.dto.RegisterRequest;
import com.example.smartagent.entity.UserInfo;
import com.example.smartagent.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * 用户认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "用户认证", description = "注册、登入、登出等认证相关接口")
public class UserAuthController {

    // 注入SecurityContextRepository（核心：用于将认证信息存入Session）
    @Autowired
    private SecurityContextRepository securityContextRepository;

    private static final Logger log = Logger.getLogger(UserAuthController.class.getName());

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册，需要提供用户名、邮箱、密码等信息")
    public ApiResponse<UserInfo> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // 验证输入
            if (request == null) {
                return ApiResponse.error("请求参数不能为空");
            }

            // 调用服务进行注册
            UserInfo userInfo = userAuthService.register(request);

            // 返回成功响应（不返回密码）
            UserInfo response = new UserInfo();
            response.setId(userInfo.getId());
            response.setUsername(userInfo.getUsername());
            response.setEmail(userInfo.getEmail());
            response.setPhone(userInfo.getPhone());
            response.setUserStatus(userInfo.getUserStatus());
            response.setCreatedAt(userInfo.getCreatedAt());

            return ApiResponse.success(response, "注册成功");
        } catch (IllegalArgumentException e) {
            log.warning("用户注册失败: " + e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "用户注册异常", e);
            return ApiResponse.error("注册失败，请稍后重试");
        }
    }

    /**
 * 用户登入
 */
@PostMapping("/login")
@Operation(summary = "用户登入", description = "用户使用用户名/邮箱/手机号和密码进行登入")
public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse httpResponse, // 新增：需要传入response用于保存上下文
        HttpSession session) {
    try {
        // 验证输入
        if (request == null) {
            return ApiResponse.error("请求参数不能为空");
        }

        // 获取客户端IP
        String clientIp = getClientIp(httpRequest);

        // 调用服务进行登入
        LoginResponse loginResponse = userAuthService.login(request, clientIp);

        // ========== 核心新增：构建Security认证对象 ==========
        // 1. 构建UserDetails（Security识别的用户信息，包含用户名和权限）
        // 注意：roles需要替换为你实际的用户角色（如"USER"、"ADMIN"）
        UserDetails userDetails = User.withUsername(loginResponse.getUsername())
                .password("N/A") // 密码无需存储（已加密在数据库），填任意值即可
                .roles("USER") // 根据实际业务替换为用户真实角色
                .build();
        
        // 2. 构建Authentication认证对象（Security核心）
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,       // 主体（用户信息）
                null,              // 凭证（密码，认证成功后设为null）
                userDetails.getAuthorities() // 用户权限/角色
        );
        
        // 3. 将认证对象存入SecurityContextHolder（当前线程）
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 4. 将认证信息存入Session（关键：让后续请求能获取到）
        securityContextRepository.saveContext(
                SecurityContextHolder.getContext(), 
                httpRequest, 
                httpResponse
        );
        // ========== 核心新增结束 ==========

        // 将用户ID存储到Session中（保留你的原有逻辑）
        session.setAttribute("userId", loginResponse.getUserId());
        session.setAttribute("username", loginResponse.getUsername());

        // 设置Session超时时间（30分钟）
        session.setMaxInactiveInterval(1800);

        log.info("用户登入成功: userId=" + loginResponse.getUserId() + ", sessionId=" + session.getId());

        return ApiResponse.success(loginResponse, "登入成功");
    } catch (IllegalArgumentException e) {
        log.warning("用户登入失败: " + e.getMessage());
        return ApiResponse.error(401, e.getMessage());
    } catch (Exception e) {
        log.log(java.util.logging.Level.SEVERE, "用户登入异常", e);
        return ApiResponse.error("登入失败，请稍后重试");
    }
}

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，清除Session")
    public ApiResponse<Void> logout(HttpSession session) {
        try {
            Object userId = session.getAttribute("userId");
            if (userId instanceof Long) {
                userAuthService.logout((Long) userId);
            }

            // 清除Session
            session.invalidate();

            log.info("用户登出成功");
            return ApiResponse.success(null, "登出成功");
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "用户登出异常", e);
            return ApiResponse.error("登出失败，请稍后重试");
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户", description = "获取当前登入用户的信息")
    public ApiResponse<UserInfo> getCurrentUser(HttpSession session) {
        try {
            Object userId = session.getAttribute("userId");
            if (userId == null || !(userId instanceof Long)) {
                return ApiResponse.error(401, "未登入");
            }

            UserInfo userInfo = userAuthService.getCurrentUser();
            if (userInfo == null) {
                return ApiResponse.error(401, "用户不存在");
            }

            // 不返回密码
            userInfo.setPasswordHash(null);

            return ApiResponse.success(userInfo);
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "获取当前用户异常", e);
            return ApiResponse.error("获取用户信息失败");
        }
    }

    /**
     * 检查用户名是否已存在
     */
    @GetMapping("/check-username/{username}")
    @Operation(summary = "检查用户名", description = "检查用户名是否已存在")
    public ApiResponse<Boolean> checkUsername(@PathVariable("username") String username) {
        try {
            boolean exists = userAuthService.usernameExists(username);
            return ApiResponse.success(exists, exists ? "用户名已存在" : "用户名可用");
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "检查用户名异常", e);
            return ApiResponse.error("检查失败");
        }
    }

    /**
     * 检查邮箱是否已存在
     */
    @GetMapping("/check-email/{email}")
    @Operation(summary = "检查邮箱", description = "检查邮箱是否已存在")
    public ApiResponse<Boolean> checkEmail(@PathVariable("email") String email) {
        try {
            boolean exists = userAuthService.emailExists(email);
            return ApiResponse.success(exists, exists ? "邮箱已被注册" : "邮箱可用");
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "检查邮箱异常", e);
            return ApiResponse.error("检查失败");
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        // 尝试从多个可能的请求头中获取IP
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For 可能包含多个IP，取第一个
            return ip.split(",")[0];
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        return ip != null ? ip : "127.0.0.1";
    }
}
