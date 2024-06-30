package tech.asari.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.asari.todo.controller.AuthMiddleware;

@Configuration
public class AuthMiddlewareConfig implements WebMvcConfigurer {

    @Bean
    public AuthMiddleware authMiddleware() {
        return new AuthMiddleware();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authMiddleware())
                .addPathPatterns("/api/**");
    }
}
