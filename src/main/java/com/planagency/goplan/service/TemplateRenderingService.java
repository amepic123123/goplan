package com.planagency.goplan.service;

import org.springframework.stereotype.Service;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.Collections;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.io.StringWriter;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateRenderingService {
    

    private final Configuration freeMarkerConfig;

    public String renderTemplateFromGithub(String templateName, String rawTemplateContent, Map<String, Object> variables){
        if (rawTemplateContent == null) {
            log.error("Raw template content is null for template: {}", templateName);
            throw new IllegalArgumentException("Raw template content is null for template: " + templateName);
        }

        try{
            Template template = new Template(templateName, new StringReader(rawTemplateContent), freeMarkerConfig);
            StringWriter writer = new StringWriter();
            template.process(variables != null ? variables : Collections.emptyMap(), writer);
            return writer.toString();
        }catch(Exception e){
            log.error("Error rendering template from github: {}", templateName, e);
            throw new RuntimeException("Template Engine failure " + templateName, e);
        }
    }
}
