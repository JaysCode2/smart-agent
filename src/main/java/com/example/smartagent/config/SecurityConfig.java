package com.example.smartagent.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // 2. 核心：将SecurityContext存入Session（解决认证信息持久化）
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // 3. 解决Session销毁时的并发问题（必须）
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 4. 修复后的安全过滤链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关键：设置SecurityContextRepository，让认证信息存入Session
        http.securityContext(context -> context
                .securityContextRepository(securityContextRepository())
                // 每次请求后清空ThreadLocal中的Context，避免内存泄漏
                .requireExplicitSave(true)
        );

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 核心：配置SessionCreationPolicy为IF_REQUIRED，允许创建Session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) // 单账号仅允许1个会话
                        .expiredUrl("/api/auth/login?expired=true")
                )
                .authorizeHttpRequests(authz -> authz
                        // 公开接口（全部放行）
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/doc.html", "/webjars/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        // 其他请求必须认证
                        .anyRequest().authenticated()
                )
                // 禁用默认表单登录和Basic认证（使用自定义接口）
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        // 关键：设置SecurityContextHolder的模式为INHERITABLETHREADLOCAL，支持多线程传递
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        return http.build();
    }

    // 修复CORS配置（生产环境建议缩小允许的Origin）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 开发环境放行前端地址，生产环境替换为真实域名
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // 关键：允许携带Cookie（Session ID）
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}