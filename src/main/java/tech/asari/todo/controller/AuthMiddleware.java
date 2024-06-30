package tech.asari.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthMiddleware implements HandlerInterceptor {

    @Value("${userBucket.path}")
    private String[] ALLOWED_EMAILS;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        DefaultOAuth2User user = ((DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String email = user.getAttribute("email");
        if (email == null || !isAllowedEmail(email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean isAllowedEmail(String email) {
        for (String allowedEmail : ALLOWED_EMAILS) {
            if (allowedEmail.equals(email)) {
                return true;
            }
        }
        return false;
    }
}
