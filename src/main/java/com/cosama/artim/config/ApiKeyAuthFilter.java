package com.cosama.artim.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final String API_KEY_HEADER = "API-Key";
    private static final String API_SECRET_HEADER = "API-Secret";
    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/v1/api/achatOnline/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);
        String apiSecret = request.getHeader(API_SECRET_HEADER);

        if ("cosama-api-key".equals(apiKey) && "cosama-api-secret".equals(apiSecret)) {
            log.info("✅ Authenticated with API Key: {}", apiKey);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(apiKey, apiSecret,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        else
        {
            log.warn("❌ Invalid API Key or Secret");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key or Secret");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
