package io.ilya.mailbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailbotApplication.class, args);
	}

}
