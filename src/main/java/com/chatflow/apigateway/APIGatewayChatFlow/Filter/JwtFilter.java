package com.chatflow.apigateway.APIGatewayChatFlow.Filter;

import com.chatflow.apigateway.APIGatewayChatFlow.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    String jwtToken = authorizationHeader.substring(7);
                    if (!jwtToken.isEmpty() && jwtService.validateToken(jwtToken)) {
                        String username = jwtService.extractUserName(jwtToken);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
