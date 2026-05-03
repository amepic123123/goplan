package com.planagency.goplan.startegy;

import com.planagency.goplan.enums.*;
import org.springframework.stereotype.Component;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import com.planagency.goplan.service.TemplateRenderingService;
import java.util.zip.ZipEntry;
import java.util.Map;
import com.planagency.goplan.service.GithubTemplateFetcher;

@Component
public class SpringBootGenerator implements ProjectGeneratorStrategy {


    TemplateRenderingService templateRenderingService;
    GithubTemplateFetcher githubTemplateFetcher;

     public SpringBootGenerator(TemplateRenderingService templateRenderingService, GithubTemplateFetcher githubTemplateFetcher) {
        this.templateRenderingService = templateRenderingService;
        this.githubTemplateFetcher = githubTemplateFetcher;
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
        String rowCoreFileContent = githubTemplateFetcher.fetchTemplate("spring-boot/Application.java.ftl");
        String coreFileContent = templateRenderingService.renderTemplateFromGithub("Application.java.ftl", rowCoreFileContent, templateData);
        String packagePath = request.basePackage().replace('.', '/') + "/";
        String coreFilePath = baseFolder + "src/main/java/" + packagePath + "Application.java";

        String rowPomContent = githubTemplateFetcher.fetchTemplate("spring-boot/pom.xml.ftl");
        String pomContent = templateRenderingService.renderTemplateFromGithub("pom.xml.ftl", rowPomContent, templateData);
        String pomFilePath = baseFolder + "pom.xml";

        addFileToZip(zipOutPut, pomFilePath, pomContent);
        addFileToZip(zipOutPut, coreFilePath, coreFileContent);
    }
    
    private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes());
        zipOutputStream.closeEntry();
    }
}
