package com.example.smartagent.service;

import com.example.smartagent.dto.LoginRequest;
import com.example.smartagent.dto.LoginResponse;
import com.example.smartagent.dto.RegisterRequest;
import com.example.smartagent.entity.UserInfo;

/**
 * 用户认证服务接口
 */
public interface UserAuthService {

    /**
     * 用户注册
     */
    UserInfo register(RegisterRequest request);

    /**
     * 用户登入
     */
    LoginResponse login(LoginRequest request, String clientIp);

    /**
     * 用户登出
     */
    void logout(Long userId);

    /**
     * 获取当前登入用户
     */
    UserInfo getCurrentUser();

    /**
     * 验证密码
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 加密密码
     */
    String encodePassword(String rawPassword);

    /**
     * 检查用户名是否已存在
     */
    boolean usernameExists(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean emailExists(String email);

    /**
     * 检查用户账号是否被锁定
     */
    boolean isAccountLocked(Long userId);

    /**
     * 记录登入失败
     */
    void recordLoginFailure(String identifier, String clientIp, String reason);

    /**
     * 重置登入失败次数
     */
    void resetLoginFailureCount(Long userId);
}
