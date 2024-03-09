package com.ecomhubconnect.EcomHubConnect.Session;

import com.ecomhubconnect.EcomHubConnect.Config.CustomUser;
import com.ecomhubconnect.EcomHubConnect.Config.CustomUserDetailsService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {
    private final InMemorySessionRegistry sessionRegistry;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SessionFilter(final InMemorySessionRegistry sessionRegistry,
                         final CustomUserDetailsService customUserDetailsService) {
        this.sessionRegistry = sessionRegistry;
        this.customUserDetailsService = customUserDetailsService;
    }
    
    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
//        System.out.println(sessionId);
        if (sessionId == null || sessionId.length() == 0) {
            chain.doFilter(request, response);
            return;
        }
//        System.out.println(request.getRequestURI());
        if (request.getRequestURI().equals("/logout")) {
            sessionRegistry.removeSession(sessionId);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        final String username = sessionRegistry.getUsernameForSession(sessionId);
        if (username == null) {
            chain.doFilter(request, response);
            return;
        }

        final CustomUser currentUser = customUserDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                currentUser,
                null,
                currentUser.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}