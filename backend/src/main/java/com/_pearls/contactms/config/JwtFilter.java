package com._pearls.contactms.config;

import com._pearls.contactms.service.CustomUserDetailsService;
import com._pearls.contactms.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext applicationContext;

    public JwtFilter(JwtService jwtService, ApplicationContext applicationContext) {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔑 Skip JWT validation for auth endpoints
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String identifier = null;

        System.out.println("Incoming Request: " + request.getMethod() + " " + request.getRequestURI());

        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            identifier = jwtService.extractIdentifier(token);
        }

        // not already authenticated
        if(identifier != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class)
                    .loadUserByUsername(identifier);

            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
