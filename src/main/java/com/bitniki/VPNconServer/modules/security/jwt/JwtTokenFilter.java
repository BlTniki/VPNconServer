package com.bitniki.VPNconServer.modules.security.jwt;

import com.bitniki.VPNconServer.modules.security.exception.JwtAuthException;
import com.bitniki.VPNconServer.modules.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, который перехватывает запросы и проверяет валидность JWT-токенов для аутентификации.
 */
@Component
public class JwtTokenFilter extends GenericFilterBean {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Фильтрует входящий запрос, проверяя валидность JWT-токена.
     * Если токен действителен, устанавливает аутентификацию в контексте безопасности.
     * Если токен истек или недействителен, возвращает ошибку ответа.
     * @param request Входящий сервлет-запрос.
     * @param response Сервлет-ответ.
     * @param chain Цепочка фильтров.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.extractToken((HttpServletRequest) request);
        try {
            if(token != null) {
                if(!jwtTokenProvider.validateToken(token)){
                    throw new JwtAuthException("Token expired", HttpStatus.UNAUTHORIZED);
                }
                // get auth
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                if(authentication != null) {
                    // check is user change token
                    // or is user have token and is equal with given
                    if(((SecurityUser)authentication.getPrincipal()).getToken() == null ||
                            !((SecurityUser)authentication.getPrincipal()).getToken().equals(token)) {
                        throw new JwtAuthException("Token expired", HttpStatus.UNAUTHORIZED);
                    }
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(e.getHttpStatus().value(), e.getMessage());
            return;
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }
}
