package com.recruitmentsystem.security.config;

import com.recruitmentsystem.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity
//        (
//                prePostEnabled = true,
//                securedEnabled = true,
//                jsr250Enabled = true
//        )
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()

//                .requestMatchers("/api/v*/admin/manage/**").hasAuthority("ADMIN")
//                .requestMatchers("/api/v*/company/manage/**").hasAuthority("COMPANY")
//                .requestMatchers("/api/v*/manage/**").hasAnyAuthority("ADMIN", "COMPANY")

                // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .requestMatchers(
                        "/v*/api-docs",
                        "/v*/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/api/**"
                )
                .permitAll()

                // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .anyRequest().authenticated()
                .and()
                // disable session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(
                        (request, response, authentication) ->
                                SecurityContextHolder.clearContext())
        ;
        return http.build();
    }
}
