package org.senlacourse.social.config;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource({"classpath:application-test.properties",
        "classpath:social.properties",
        "classpath:social-security.properties"})
@ComponentScan(basePackages = "org.senlacourse.social.*")
@Profile("test")
public class SocialTestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
