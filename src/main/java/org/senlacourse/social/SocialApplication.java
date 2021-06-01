package org.senlacourse.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SocialApplication.class);
		application.setAdditionalProfiles("dev");
		application.run(args);
	}

}
