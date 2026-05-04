package com.planagency.goplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class GoplanApplication {

	private static final Logger log = LoggerFactory.getLogger(GoplanApplication.class);
	private final RedisTemplate<String, String> redisTemplate;

	public GoplanApplication(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	public void clearCache() {
    redisTemplate.getConnectionFactory()
        .getConnection()
		.flushDb();
	log.info("Cache cleared on startup");
		}
	public static void main(String[] args) {
		SpringApplication.run(GoplanApplication.class, args);
		log.info("GoPlan application started successfully we love abood");
	}

}
