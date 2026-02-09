    package com.example.smartagent.interceptor;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.stereotype.Component;
    import org.springframework.web.servlet.HandlerInterceptor;

    import java.util.logging.Logger;

    /**
     * 请求日志和安全拦截器
     */
    @Component
    public class RequestSecurityInterceptor implements HandlerInterceptor {

        private static final Logger log = Logger.getLogger(RequestSecurityInterceptor.class.getName());

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 记录请求信息
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String ip = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            log.info("请求开始: method=" + method + ", uri=" + uri + ", ip=" + ip + ", userAgent=" + userAgent);

            // 设置安全相关Header
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
            response.setHeader("X-XSS-Protection", "1; mode=block");

            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();

            log.info("请求完成: method=" + method + ", uri=" + uri + ", status=" + status);

            if (ex != null) {
                log.log(java.util.logging.Level.SEVERE, "请求异常: method=" + method + ", uri=" + uri, ex);
            }
        }

        /**
         * 获取客户端IP
         */
        private String getClientIp(HttpServletRequest request) {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }

            ip = request.getHeader("X-Real-IP");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            return request.getRemoteAddr();
        }
    }
