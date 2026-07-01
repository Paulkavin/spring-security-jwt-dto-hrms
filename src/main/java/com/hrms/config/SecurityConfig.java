package com.hrms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.hrms.security.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Bean is a shared object, spring will inject where ever needed
@Configuration //This class creates Beans (objects) for the Spring container.
@EnableMethodSecurity // spring scans and if it has @pre authorize it creates proxy (gaurd like)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //Why return PasswordEncoder - single Spring-managed PasswordEncoder that can be injected 
    //anywhere instead of creating new objects manually, company may change he bcrypt to argon or something.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration)
        throws Exception {

    return configuration.getAuthenticationManager();
}
    //csrf.disable() - disable cokkies ,instead we use JWT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    http
            .csrf(csrf -> csrf.disable())//disables cookies
            //makes stateless
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                    //make login page doesn't require login ,if already login
                    .requestMatchers("/auth/**").permitAll()
                    //other requests requires like /employees requires authentication
                    .anyRequest().authenticated()

            )
            // .authenticationProvider(authenticationProvider)
              .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

    return http.build();

}


}