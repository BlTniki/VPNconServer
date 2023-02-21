package com.bitniki.VPNconServer.modules.security;

import com.bitniki.VPNconServer.exception.JwtAuthException;
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

@Component
public class JwtTokenFilter extends GenericFilterBean {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
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
            ((HttpServletResponse) response).sendError(e.getHttpStatus().value());
        } catch (UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value());
        }
        chain.doFilter(request, response);
    }
}
