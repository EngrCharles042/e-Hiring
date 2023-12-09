package com.swiftselect.infrastructure.configurations.securityconfig;

import com.swiftselect.infrastructure.security.JwtAuthenticationEntryPoint;
import com.swiftselect.infrastructure.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.models.PathItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableAsync
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {
    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    public Config(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Execute JwtAuthenticationFilter before the UsernamePasswordAuthenticationFilter of spring security
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        httpSecurity.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antMatcher(HttpMethod.POST, "/auth/**"),
                                antMatcher(HttpMethod.GET, "/auth/**"))
                        .permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/job-post/employer"))
                        .hasAnyAuthority("JOB_SEEKER", "EMPLOYER")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/notification/**"))
                        .permitAll()
                        .requestMatchers(antMatcher("/job-post/filter/**"))
                        .permitAll()
                        .requestMatchers(antMatcher("/chat/**"))
                        .permitAll()
                        .requestMatchers(antMatcher("/job-seeker/**"))
                        .hasAuthority("JOB_SEEKER")
                        .requestMatchers(antMatcher("/employer/**"))
                        .hasAuthority("EMPLOYER")
                        .requestMatchers(antMatcher("/swagger-ui/**"),
                                antMatcher("/v3/api-docs/**"))
                        .permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/job-post/**"),
                                antMatcher(HttpMethod.PUT, "/job-post/**"))
                        .hasAnyAuthority("EMPLOYER")
                        .requestMatchers(antMatcher("/users/**"))
                        .hasAnyAuthority("JOB_SEEKER", "EMPLOYER")
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    // Configuring CORS to enable foreign applications to access our endpoints
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:1123"));
        configuration.setAllowedMethods(List.of("POST", "GET", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
