package com.example.smartagent.dto;

import jakarta.validation.constraints.*;

/**
 * 用户登入请求DTO
 */
public class LoginRequest {

    /**
     * 用户名或邮箱或手机号
     */
    @NotBlank(message = "用户名/邮箱/手机号不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度不正确")
    private String password;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 是否记住登录状态
     */
    private Boolean rememberMe;

    // ==================== 构造器 ====================
    public LoginRequest() {
    }

    public LoginRequest(String username, String password, String verificationCode, Boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.verificationCode = verificationCode;
        this.rememberMe = rememberMe;
    }

    // ==================== Getter和Setter方法 ====================
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
