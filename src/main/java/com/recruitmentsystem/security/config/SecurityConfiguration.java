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
                .requestMatchers("/api/v*/auth/**",
                        "/v*/api-docs",
                        "/v*/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/api/v*/users/getAccountInfo",
                        "/api/v*/categories")
                .permitAll()

                .requestMatchers("/api/v*/users/**").hasRole("ADMIN")
                .requestMatchers("/api/v*/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/v*/companies/**").hasAnyRole("ADMIN", "HR")
                .requestMatchers(("/api/v*/categories/**")).hasRole("ADMIN")

                // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .anyRequest().authenticated()
                .and()
                // disable session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

//				.logout().permitAll();
//				.and()
//				.exceptionHandling()
//				.authenticationEntryPoint(
//					(request, response, ex) -> {
//						response.sendError(
//							HttpServletResponse.SC_UNAUTHORIZED,
//							ex.getMessage()
//						);
//					}
//				);


        return http.build();
    }
}
