package com.cosama.artim.security;

import com.cosama.artim.config.ApiKeyAuthFilter;
import com.cosama.artim.config.JwtAuthenticationFilter;
import com.cosama.artim.models.Role;
import com.cosama.artim.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    /**
     * 🔹 Configuration pour l'authentification via API Key (`/v1/api/achatOnline/**`).
     */
    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration cors1 = new CorsConfiguration();
                    cors1.setAllowedOriginPatterns(List.of("*"));
                    cors1.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    cors1.setAllowedHeaders(List.of("Authorization", "Content-Type", "API-Key", "API-Secret"));
                    cors1.setExposedHeaders(List.of("Authorization", "Content-Type", "API-Key", "API-Secret"));
                    cors1.setAllowCredentials(false);
                    return cors1;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/api/achatOnline/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 🔹 Configuration pour les requêtes nécessitant un JWT (ex : `/api/v1/auth/**`).
     */
    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration cors1 = new CorsConfiguration();
                    cors1.setAllowedOriginPatterns(List.of("*"));
                    cors1.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    cors1.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    cors1.setExposedHeaders(List.of("Authorization"));
                    cors1.setAllowCredentials(true);
                    return cors1;
                }))
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/sms/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/parametrage/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/chefDeGare/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/billetterie/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/fret/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/clientEnCompte/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/etat/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/v1/api/qrcode/**").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 🔹 Fournisseur d'authentification basé sur la base de données.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * 🔹 Gestionnaire d'authentification.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 🔹 Encodeur de mot de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
