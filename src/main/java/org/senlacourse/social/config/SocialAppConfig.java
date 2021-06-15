package org.senlacourse.social.config;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "org.senlacourse.social.*")
@PropertySource("classpath:application-dev.properties")
@PropertySource("classpath:social-security.properties")
@PropertySource("classpath:social.properties")
@Profile("dev")
public class SocialAppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
