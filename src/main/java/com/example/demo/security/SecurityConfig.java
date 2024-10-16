package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.demo.security.jwt.jwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public UsuarioDetailsService empleadoDetailsService;

    @Autowired
    private jwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    protected SecurityFilterChain securityfilterchain(HttpSecurity httpsecurity) throws Exception {

        httpsecurity.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and()
                .csrf().disable().authorizeHttpRequests()
                .requestMatchers("/index", "/img/**",
                        "/css/**", "/js/**", "/favicon.ico", "/empleado/login", "/prueba",
                        "/login_admin", "/reform", "/chef", "/contacto", "/carta", "/empleado/registrar")
                .permitAll()
                .requestMatchers("/prueba", "menu_login", "/platos", "/hola", "/img/**",
                        "/css/**", "/js/**", "/favicon.ico")
                .permitAll().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpsecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpsecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

}