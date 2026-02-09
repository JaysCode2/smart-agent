package com.example.smartagent.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志表实体
 */
@Entity
@Table(name = "login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "username", length = 64)
    private String username;

    /**
     * 登录IP地址
     */
    @Column(name = "login_ip", length = 50)
    private String loginIp;

    /**
     * 登录时间
     */
    @Column(name = "login_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loginTime;

    /**
     * 登录结果（0-失败，1-成功）
     */
    @Column(name = "login_result", columnDefinition = "TINYINT")
    private Integer loginResult;

    /**
     * 失败原因
     */
    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    /**
     * User Agent
     */
    @Column(name = "user_agent", length = 255)
    private String userAgent;

    // ==================== 构造器 ====================
    public LoginLog() {
    }

    public LoginLog(Long id, Long userId, String username, String loginIp, LocalDateTime loginTime,
                   Integer loginResult, String failureReason, String userAgent) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
        this.loginResult = loginResult;
        this.failureReason = failureReason;
        this.userAgent = userAgent;
    }

    // ==================== Getter和Setter方法 ====================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(Integer loginResult) {
        this.loginResult = loginResult;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    // ==================== Builder-like methods ====================
    public static LoginLog builder() {
        return new LoginLog();
    }

    public LoginLog id(Long id) { this.id = id; return this; }
    public LoginLog userId(Long userId) { this.userId = userId; return this; }
    public LoginLog username(String username) { this.username = username; return this; }
    public LoginLog loginIp(String loginIp) { this.loginIp = loginIp; return this; }
    public LoginLog loginTime(LocalDateTime loginTime) { this.loginTime = loginTime; return this; }
    public LoginLog loginResult(Integer loginResult) { this.loginResult = loginResult; return this; }
    public LoginLog failureReason(String failureReason) { this.failureReason = failureReason; return this; }
    public LoginLog userAgent(String userAgent) { this.userAgent = userAgent; return this; }
    public LoginLog build() { return this; }
}
