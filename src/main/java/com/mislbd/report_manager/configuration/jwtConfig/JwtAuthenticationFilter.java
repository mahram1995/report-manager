
package com.mislbd.report_manager.configuration.jwtConfig;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditorContextHolder;
import com.mislbd.report_manager.service.admin.CustomUserDetailsService;

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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            final String authHeader =request.getHeader(HttpHeaders.AUTHORIZATION);

            // --------------------------------------------------
            // 1. No JWT supplied: continue without authentication
            // --------------------------------------------------
            if (authHeader == null || authHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // --------------------------------------------------
            // 2. Ignore headers that are not Bearer authentication
            // --------------------------------------------------
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7).trim();

            if (jwt.isEmpty()) {
                sendUnauthorizedResponse(response);
                return;
            }

            // --------------------------------------------------
            // 3. Extract username from JWT
            // --------------------------------------------------
            String username = jwtUtil.extractUsername(jwt);

            if (username == null || username.isBlank()) {
                sendUnauthorizedResponse(response);
                return;
            }

            AuditorContextHolder.setCurrentUser(username);

            // --------------------------------------------------
            // 4. Authenticate user if not already authenticated
            // --------------------------------------------------
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                if (!jwtUtil.isTokenValid(jwt, userDetails)) {
                    sendUnauthorizedResponse(response);
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }

            // --------------------------------------------------
            // 5. Continue request
            // --------------------------------------------------
            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            System.err.println(
                    "JWT FILTER ERROR: " + ex.getClass().getName()
            );

            System.err.println(
                    "JWT FILTER MESSAGE: " + ex.getMessage()
            );

            SecurityContextHolder.clearContext();

            sendUnauthorizedResponse(response);
        }
    }

    private void sendUnauthorizedResponse(
            HttpServletResponse response
    ) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write("""
                {
                  "status": 401,
                  "errorCode": "TOKEN_INVALID",
                  "message": "Un-Authorized! Please Login Again."
                }
                """);
    }
}
