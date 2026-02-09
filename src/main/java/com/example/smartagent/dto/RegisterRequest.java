package com.example.smartagent.dto;

import jakarta.validation.constraints.*;

/**
 * 用户注册请求DTO
 */
public class RegisterRequest {

    /**
     * 用户名（4-32个字符，只能包含字母、数字、下划线）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 32, message = "用户名长度必须在4-32个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号（可选）
     */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码（8-32个字符，必须包含大小写字母、数字、特殊字符）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度必须在8-32个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[a-zA-Z\\d@$!%*?&]+$", 
            message = "密码必须包含大小写字母、数字和特殊字符(@$!%*?&)")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 验证码（可选）
     */
    private String verificationCode;

    /**
     * 同意服务条款
     */
    @AssertTrue(message = "必须同意服务条款")
    private Boolean agreeTerms;

    // ==================== 构造器 ====================
    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String phone, String password,
                          String confirmPassword, String verificationCode, Boolean agreeTerms) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.verificationCode = verificationCode;
        this.agreeTerms = agreeTerms;
    }

    // ==================== Getter和Setter方法 ====================
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Boolean getAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(Boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }
}
