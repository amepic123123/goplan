package com.planagency.goplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class GoplanApplication {

	private static final Logger log = LoggerFactory.getLogger(GoplanApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GoplanApplication.class, args);
		log.info("GoPlan application started successfully");
	}

}
