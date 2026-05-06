package com.planagency.goplan.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import com.planagency.goplan.enums.Frameworks;
import com.planagency.goplan.dto.ManifestDto;
import com.planagency.goplan.dto.TemplateFileDto;

@Service
@Slf4j
public class GithubTemplateFetcher {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private Map<String, String> templateCache;

    public GithubTemplateFetcher(@Qualifier("gitHubClient") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }


    @Cacheable(value = "manifest", key = "#manifestName")
    public String fetchManifest(String manifestName){
        log.info("Fetching remote manifest from github {}", manifestName);
        try{
            return webClient.get()
            .uri(manifestName)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        } catch (Exception e) {
            log.error("Error fetching manifest: {}", manifestName, e);
            throw new RuntimeException("Failed to fetch manifest", e);
        }
    }
    public Map<String, String> turnManifestToMap(String manifestContent){
        try {
            ManifestDto manifest = objectMapper.readValue(manifestContent, ManifestDto.class);
            return manifest.getFiles().stream()
                .collect(Collectors.toMap(TemplateFileDto::getPath, TemplateFileDto::getContent));
        } catch (Exception e) {
            log.error("Error parsing manifest content", e);
            throw new RuntimeException("Failed to parse manifest content", e);
        }
    }
    public String fetchTemplate(String templatePath){
        log.info("Fetching template from Manifest: {}", templatePath);
       if(templateCache == null){
        loadManifest(Frameworks.SPRING_BOOT);
       }
       return templateCache.get(templatePath);
    }

    private void loadManifest(Frameworks framework){
        String manifestPath = framework.name().toLowerCase().replace("_", "-") + "/manifest.json";
        String manifestContent = fetchManifest(manifestPath);
        this.templateCache = turnManifestToMap(manifestContent);
    }

}
