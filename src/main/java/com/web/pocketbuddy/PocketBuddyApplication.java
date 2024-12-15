package com.web.pocketbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.web.pocketbuddy.dao")
public class PocketBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketBuddyApplication.class, args);
	}

}
