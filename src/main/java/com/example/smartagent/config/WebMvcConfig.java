package com.example.smartagent.config;

import com.example.smartagent.interceptor.RequestSecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestSecurityInterceptor requestSecurityInterceptor;

    // 构造器注入（Lombok的@RequiredArgsConstructor也可以）
    public WebMvcConfig(RequestSecurityInterceptor requestSecurityInterceptor) {
        this.requestSecurityInterceptor = requestSecurityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestSecurityInterceptor)
                .addPathPatterns("/**")
                // 关键：排除所有认证相关接口，避免拦截器干扰
                .excludePathPatterns(
                        "/api/auth/**",
                        "/swagger-ui/**", "/v3/api-docs/**", "/doc.html", "/webjars/**",
                        "/actuator/**"
                );
    }
}