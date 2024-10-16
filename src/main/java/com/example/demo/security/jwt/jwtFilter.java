package com.example.demo.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.UsuarioDetailsService;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter {

    @Autowired
    public jwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailsService empleadodetailservice;

    private String username = null;

    Claims claims = null;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Permitir acceso a rutas específicas y a cualquier archivo CSS

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            claims = jwtUtil.extractAllClaims(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userdetails = empleadodetailservice.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, userdetails)) {
                    UsernamePasswordAuthenticationToken usernamepasswordauthenticationtoken = new UsernamePasswordAuthenticationToken(
                            userdetails, null, userdetails.getAuthorities());
                    usernamepasswordauthenticationtoken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamepasswordauthenticationtoken);

                }
            }
        }

        filterChain.doFilter(request, response);

    }

    public Boolean isadmin() {
        return "ROLE_admin".equalsIgnoreCase((String) claims.get("rol"));
    }

    public Boolean isUser() {
        return "ROLE_user".equalsIgnoreCase((String) claims.get("rol"));
    }

    public String getCurrentUsername() {
        return (String) claims.get("name");
    }

}
