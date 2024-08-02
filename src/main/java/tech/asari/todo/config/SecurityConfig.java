package tech.asari.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.csrf.enabled:false}")
    private boolean csrfEnabled;

    private static final String[] PERMIT_ALL_PATH = {
            "/",
            "/error",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(
                csrfEnabled ? Customizer.withDefaults() : AbstractHttpConfigurer::disable
        ).authorizeHttpRequests(auth -> auth
                .requestMatchers(PERMIT_ALL_PATH).permitAll()
                .anyRequest().authenticated()
        ).oauth2Login(login -> login
                .defaultSuccessUrl("/")
        );
        return http.build();
    }
}
