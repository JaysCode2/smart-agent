package com.example.smartagent.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 / Swagger配置
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Smart Agent 用户认证系统",
                version = "1.0.0",
                description = "用户注册、登入、登出等认证系统API文档",
                contact = @Contact(
                        name = "Jay开发团队",
                        email = "2636667032@qq.com"
                )
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Smart Agent API")
                        .version("1.0.0")
                        .description("用户认证系统接口文档")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Jay")
                                .email("2636667032@qq.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
