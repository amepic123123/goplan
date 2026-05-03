package com.planagency.goplan.service;

import org.springframework.stereotype.Service;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import java.io.StringWriter;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateRenderingService {
    

    private final Configuration freeMarkerConfig;

    public String renderTemplate(String templateName, Map<String, Object> variables){

    try{
        
        Template template = freeMarkerConfig.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(variables, writer);
        return writer.toString();

    }catch(Exception e){

        log.error("Error loading template: {}", templateName, e);
        throw new RuntimeException("Template Engine failure " + templateName, e);
    }

}
    
}
