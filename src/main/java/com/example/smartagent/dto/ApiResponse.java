package com.example.smartagent.dto;

/**
 * 通用响应DTO
 */
public class ApiResponse<T> {

    /**
     * 响应码（0成功，非0失败）
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    // ==================== 构造器 ====================
    public ApiResponse() {
    }

    public ApiResponse(Integer code, String message, T data, Long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    // ==================== Getter和Setter方法 ====================
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // ==================== Builder方法 ====================
    public static <T> ApiResponse.Builder<T> builder() {
        return new ApiResponse.Builder<>();
    }

    /**
     * Builder内部类
     */
    public static class Builder<T> {
        private Integer code;
        private String message;
        private T data;
        private Long timestamp;

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(this.code, this.message, this.data, this.timestamp);
        }
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 成功的响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "成功", data, System.currentTimeMillis());
    }

    /**
     * 成功的响应（带自定义消息）
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(0, message, data, System.currentTimeMillis());
    }

    /**
     * 失败的响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, System.currentTimeMillis());
    }

    /**
     * 失败的响应（默认code=1）
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(1, message);
    }
}
