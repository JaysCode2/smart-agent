package com.example.smartagent.dto;

import java.time.LocalDateTime;

/**
 * 用户登入响应DTO
 */
public class LoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * Session ID（可选）
     */
    private String sessionId;

    /**
     * 登入时间
     */
    private LocalDateTime loginTime;

    // ==================== 构造器 ====================
    public LoginResponse() {
    }

    public LoginResponse(Long userId, String username, String email, String realName, 
                        String avatarUrl, LocalDateTime lastLoginTime, String sessionId, 
                        LocalDateTime loginTime) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.realName = realName;
        this.avatarUrl = avatarUrl;
        this.lastLoginTime = lastLoginTime;
        this.sessionId = sessionId;
        this.loginTime = loginTime;
    }

    // ==================== Getter和Setter方法 ====================
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    // ==================== Builder-like methods ====================
    public static LoginResponse builder() {
        return new LoginResponse();
    }

    public LoginResponse userId(Long userId) { this.userId = userId; return this; }
    public LoginResponse username(String username) { this.username = username; return this; }
    public LoginResponse email(String email) { this.email = email; return this; }
    public LoginResponse realName(String realName) { this.realName = realName; return this; }
    public LoginResponse avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
    public LoginResponse lastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; return this; }
    public LoginResponse sessionId(String sessionId) { this.sessionId = sessionId; return this; }
    public LoginResponse loginTime(LocalDateTime loginTime) { this.loginTime = loginTime; return this; }
    public LoginResponse build() { return this; }
}
