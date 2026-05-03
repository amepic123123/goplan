package com.planagency.goplan.startegy;

import com.planagency.goplan.enums.*;
import org.springframework.stereotype.Component;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import com.planagency.goplan.service.TemplateRenderingService;
import java.util.zip.ZipEntry;
import java.util.Map;

@Component
public class SpringBootGenerator implements ProjectGeneratorStrategy {


    TemplateRenderingService templateRenderingService;

     public SpringBootGenerator(TemplateRenderingService templateRenderingService) {
        this.templateRenderingService = templateRenderingService;
    }

    @Override
    public Frameworks getSupportedFramework() {
        return Frameworks.SPRING_BOOT;
    }

    @Override
    public void generateProject(ZipOutputStream zipOutPut, ProjectRequestDto request) throws IOException {
        String baseFolder = request.projectName() + "/";

        Map<String, Object> templateData = Map.of(
            "projectName", request.projectName(),
            "basePackage", request.basePackage()
        );
        String coreFolderContent = templateRenderingService.renderTemplate("Application.java.ftl", templateData);
        String packagePath = request.basePackage().replace('.', '/') + "/";
        String coreFilePath = baseFolder + "src/main/java/" + packagePath + "Application.java";

        String pomContent = templateRenderingService.renderTemplate("pom.xml.ftl", templateData);
        addFileToZip(zipOutPut, baseFolder + "pom.xml", pomContent);
        addFileToZip(zipOutPut, coreFilePath, coreFolderContent);
        
    }
    
    private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes());
        zipOutputStream.closeEntry();
    }
}
