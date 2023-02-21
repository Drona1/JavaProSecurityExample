package com.gmail.dimabah;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/avatar/**")
                .addResourceLocations("file:/D:/upload_dir/");
    }
    public static final String ADMIN = "admin";
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner start(final UserService userService,
                                   final PasswordEncoder encoder) {
        return strings -> {
            userService.addUser(ADMIN,
                    encoder.encode("password"),
                    UserRole.ADMIN, "", "", "");
            userService.addUser("user",
                    encoder.encode("password"),
                    UserRole.USER, "", "", "");
            userService.addUser("moder",
                    encoder.encode("password"),
                    UserRole.MODERATOR, "", "", "");
        };
    }
}
