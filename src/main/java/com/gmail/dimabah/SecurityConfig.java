package com.gmail.dimabah;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests( authConfig->{
                    authConfig.requestMatchers("/admin").hasAnyAuthority("ADMIN","MODERATOR");
                    authConfig.requestMatchers("/change**").hasAnyAuthority("ADMIN","MODERATOR");
                    authConfig.requestMatchers("/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST,"/newuser").permitAll();
                    authConfig.anyRequest().authenticated();
                })
            .exceptionHandling()
                .accessDeniedPage("/unauthorized")
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error")
                .usernameParameter("j_login")
                .passwordParameter("j_password")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessUrl("/login?logout")
                .and()
        ;

        return http.build();
    }
}
