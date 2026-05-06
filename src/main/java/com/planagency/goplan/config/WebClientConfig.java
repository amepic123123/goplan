package com.planagency.goplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebClientConfig {
    
    @Bean
    public org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder() {
        return org.springframework.web.reactive.function.client.WebClient.builder();
    }

    @Bean
    public org.springframework.web.reactive.function.client.WebClient gitHubClient(org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("https://raw.githubusercontent.com/amepic123123/goplan-templates/main/")
                .build();
    }
    @Bean
    public org.springframework.data.redis.connection.RedisConnection redisConnection(org.springframework.data.redis.connection.RedisConnectionFactory redisConnectionFactory) {
        return redisConnectionFactory.getConnection();
    }

   @Bean
   public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper;
        }
}
