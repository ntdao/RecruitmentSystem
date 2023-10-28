package com.recruitmentsystem.security.config;

import com.recruitmentsystem.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
//		securedEnabled = true,
//		jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()

                .authorizeHttpRequests()

                // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .requestMatchers(
                        "/api/v*/auth/**",
                        "/v*/api-docs",
                        "/v*/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/api/image/**",
                        "/api/v*/user/**",
                        "/api/v*/categories/**",
                        "/api/v*/companies/**",
                        "/api/v*/branches/**",
                        "/api/v*/jobs/**")
                .permitAll()

                .requestMatchers("/api/v*/manage/**").hasRole("ADMIN")
//                .requestMatchers("/api/v*/manage/**/**").hasRole("ADMIN")
//                .requestMatchers("/api/v*/manage_roles/**").hasRole("ADMIN")
//                .requestMatchers("/api/v*/manage_companies/**").hasRole("ADMIN")
//                .requestMatchers("/api/v*/company/manage_branches/**").hasRole("ADMIN")
//                .requestMatchers("/api/v*/manage_categories/**").hasRole("ADMIN")
                .requestMatchers("/api/v*/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v*/hr/**").hasRole("HR")

                // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .anyRequest().authenticated()
                .and()
                // disable session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
