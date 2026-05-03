package com.planagency.goplan.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GithubTemplateFetcher {

        private final WebClient webClient;

    public GithubTemplateFetcher(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://raw.githubusercontent.com/amepic123123/goplan-templates/main/")
                .build();
    }

    @Cacheable(value = "templates", key = "#templateName")
    public String fetchTemplate(String templateName){
        log.info("Fetching remote template from github {}", templateName);
        try{
            return webClient.get()
            .uri(templateName)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        }
        catch (Exception e) {
            log.error("Error fetching template: {}", templateName, e);
            throw new RuntimeException("Failed to fetch template", e);
        }
    }

}
