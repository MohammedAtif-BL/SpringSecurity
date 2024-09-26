package com.example.springsecurityexample.config;

import com.example.springsecurityexample.service.UserInfoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    //Authentication
    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withUsername("Atif")
//                .password(encoder.encode("Pwd1"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withUsername("Sample")
//                .password(encoder.encode("Pwd2"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin,user);
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Using lambda for CSRF disable
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/products/welcome","/products/new").permitAll()  // Permitting welcome route
                        .requestMatchers("/products/**").authenticated()    // Authenticating other product routes
                )
                .httpBasic(Customizer.withDefaults()); // Enable HTTP Basic authentication instead of formLogin
//                .formLogin(Customizer.withDefaults());  // Using default formLogin configuration

        return httpSecurity.build();  // Ensure the build() call is part of the return statement
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
