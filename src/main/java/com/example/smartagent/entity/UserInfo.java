package com.example.smartagent.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息表实体
 */
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（登录账号）
     */
    @TableField("username")
    private String username;

    /**
     * 邮箱地址（唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 手机号（唯一）
     */
    @TableField("phone")
    private String phone;

    /**
     * BCrypt加密后的密码
     */
    @TableField("password_hash")
    private String passwordHash;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 头像URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 出生日期
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;

    /**
     * 用户状态（0-禁用，1-正常，2-锁定）
     */
    @TableField("user_status")
    private Integer userStatus;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 登录失败次数
     */
    @TableField("login_failure_count")
    private Integer loginFailureCount;

    /**
     * 账号锁定截止时间
     */
    @TableField("locked_until")
    private LocalDateTime lockedUntil;

    /**
     * 创建者
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新者
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除时间（软删除）
     */
    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 是否删除（0-否，1-是）
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 乐观锁版本号
     */
    @TableField("version")
    private Integer version;

    // ==================== 构造器 ====================
    public UserInfo() {
    }

    public UserInfo(Long id, String username, String email, String phone, String passwordHash, 
                   String realName, String avatarUrl, Integer gender, LocalDate birthday, 
                   String bio, Integer userStatus, LocalDateTime lastLoginTime, String lastLoginIp, 
                   Integer loginFailureCount, LocalDateTime lockedUntil, String createdBy, 
                   LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt, 
                   LocalDateTime deletedAt, Integer isDeleted, Integer version) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.realName = realName;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.birthday = birthday;
        this.bio = bio;
        this.userStatus = userStatus;
        this.lastLoginTime = lastLoginTime;
        this.lastLoginIp = lastLoginIp;
        this.loginFailureCount = loginFailureCount;
        this.lockedUntil = lockedUntil;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
        this.version = version;
    }

    // ==================== Getter和Setter方法 ====================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // ==================== Builder-like methods ====================
    public static UserInfo builder() {
        return new UserInfo();
    }

    public UserInfo id(Long id) { this.id = id; return this; }
    public UserInfo username(String username) { this.username = username; return this; }
    public UserInfo email(String email) { this.email = email; return this; }
    public UserInfo phone(String phone) { this.phone = phone; return this; }
    public UserInfo passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
    public UserInfo realName(String realName) { this.realName = realName; return this; }
    public UserInfo avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
    public UserInfo gender(Integer gender) { this.gender = gender; return this; }
    public UserInfo birthday(LocalDate birthday) { this.birthday = birthday; return this; }
    public UserInfo bio(String bio) { this.bio = bio; return this; }
    public UserInfo userStatus(Integer userStatus) { this.userStatus = userStatus; return this; }
    public UserInfo lastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; return this; }
    public UserInfo lastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; return this; }
    public UserInfo loginFailureCount(Integer loginFailureCount) { this.loginFailureCount = loginFailureCount; return this; }
    public UserInfo lockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; return this; }
    public UserInfo createdBy(String createdBy) { this.createdBy = createdBy; return this; }
    public UserInfo createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public UserInfo updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }
    public UserInfo updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
    public UserInfo deletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; return this; }
    public UserInfo isDeleted(Integer isDeleted) { this.isDeleted = isDeleted; return this; }
    public UserInfo version(Integer version) { this.version = version; return this; }
    public UserInfo build() { return this; }
}
