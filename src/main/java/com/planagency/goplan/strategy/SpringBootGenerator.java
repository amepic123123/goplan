package com.planagency.goplan.strategy;

import com.planagency.goplan.enums.*;
import org.springframework.stereotype.Component;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import com.planagency.goplan.service.TemplateRenderingService;
import java.util.zip.ZipEntry;
import java.util.Map;
import com.planagency.goplan.service.GithubTemplateFetcher;
import java.nio.charset.StandardCharsets;

@Component
public class SpringBootGenerator implements ProjectGeneratorStrategy {

    private final TemplateRenderingService templateRenderingService;
    private final GithubTemplateFetcher githubTemplateFetcher;

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
            "basePackage", request.basePackage(),
            "serverPort", "8080"
        );

        //features implementation
        if(request.features().contains(Features.CONTROLLERS)){

            addControllerFiles(zipOutPut, baseFolder, templateData, request.basePackage());
        }
        if(request.features().contains(Features.SERVICES)){
            addServiceFiles(zipOutPut, baseFolder, templateData, request.basePackage());
        }
        if(request.features().contains(Features.REPOSITORIES)){
            addRepositoryFiles(zipOutPut, baseFolder, templateData, request.basePackage());
        }
        if(request.features().contains(Features.DOCKER)){
            addDockerFile(zipOutPut, baseFolder, templateData);
        }
        if(request.features().contains(Features.MODELS)){
            
            addModelFiles(zipOutPut, baseFolder, templateData, request.basePackage());
        }
        createDirectoryStructure(zipOutPut, baseFolder);
        addCoreFiles(zipOutPut, baseFolder, templateData, request.basePackage());
        addPomFile(zipOutPut, baseFolder, templateData);
        addMavenWrapper(zipOutPut, baseFolder);
        addApplicationProperties(zipOutPut, baseFolder, templateData);
    }
    // Helper method to create directory structure
    private void createDirectoryStructure(ZipOutputStream zipOutPut, String baseFolder) throws IOException {
        String[] directories = {
            baseFolder + "src/main/java/",
            baseFolder + "src/main/resources/",
            baseFolder + "src/test/java/"
        };
        for (String dir : directories) {
            ZipEntry zipEntry = new ZipEntry(dir);
            zipOutPut.putNextEntry(zipEntry);
            zipOutPut.closeEntry();
        }
    }
    // Helper method to add Maven Wrapper files
    private void addMavenWrapper(ZipOutputStream zipOutPut, String baseFolder) throws IOException {
        String mvnwWrapperContent = githubTemplateFetcher.fetchTemplate("spring-boot/.mvn/wrapper/maven-wrapper.properties.ftl");
        String renderedMvnwWrapper = templateRenderingService.renderTemplateFromGithub("maven-wrapper.properties", mvnwWrapperContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + ".mvn/wrapper/maven-wrapper.properties", renderedMvnwWrapper);
        
        String mvnwContent = githubTemplateFetcher.fetchTemplate("spring-boot/mvnw.ftl");
        String renderedMvnw = templateRenderingService.renderTemplateFromGithub("mvnw", mvnwContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + "mvnw", renderedMvnw);
        String mvnwCmdContent = githubTemplateFetcher.fetchTemplate("spring-boot/mvnw.cmd.ftl");
        String renderedMvnwCmd = templateRenderingService.renderTemplateFromGithub("mvnw.cmd", mvnwCmdContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + "mvnw.cmd", renderedMvnwCmd);
    }
    // Helper method to add core application file
    private void addCoreFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String coreFileContent = githubTemplateFetcher.fetchTemplate("spring-boot/Application.java.ftl");
        String renderedCoreFile = templateRenderingService.renderTemplateFromGithub("Application.java.ftl", coreFileContent, templateData);
        String packagePath = basePackage.replace('.', '/') + "/";
        String coreFilePath = baseFolder + "src/main/java/" + packagePath + "Application.java";
        addFileToZip(zipOutPut, coreFilePath, renderedCoreFile);
    }
    // Helper method to add POM file
    private void addPomFile(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String pomContent = githubTemplateFetcher.fetchTemplate("spring-boot/pom.xml.ftl");
        String renderedPom = templateRenderingService.renderTemplateFromGithub("pom.xml.ftl", pomContent, templateData);
        String pomFilePath = baseFolder + "pom.xml";
        addFileToZip(zipOutPut, pomFilePath, renderedPom);
    }
    // Helper method to add controller file
    private void addControllerFiles(ZipOutputStream zipOutPut,String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
      String controllerContent = githubTemplateFetcher.fetchTemplate("spring-boot/controller/testController.java.ftl");
      String renderedController = templateRenderingService.renderTemplateFromGithub("testController.java.ftl", controllerContent, templateData);
      String controllerPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/controller/TestController.java";
            addFileToZip(zipOutPut, controllerPath, renderedController);
    }
    // Helper method to add service file
    private void addServiceFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String serviceContent = githubTemplateFetcher.fetchTemplate("spring-boot/service/testService.java.ftl");
        String renderedService = templateRenderingService.renderTemplateFromGithub("testService.java.ftl", serviceContent, templateData);
        String servicePath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/service/TestService.java";
        addFileToZip(zipOutPut, servicePath, renderedService);
    }
    // Helper method to add model file
    private void addModelFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String modelContent = githubTemplateFetcher.fetchTemplate("spring-boot/model/testModel.java.ftl");
        String renderedModel = templateRenderingService.renderTemplateFromGithub("testModel.java.ftl", modelContent, templateData);
        String modelPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/model/TestModel.java";
        addFileToZip(zipOutPut, modelPath, renderedModel);
    }
    // Helper method to add repository file
    private void addRepositoryFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String repositoryContent = githubTemplateFetcher.fetchTemplate("spring-boot/repository/testRepository.java.ftl");
        String renderedRepository = templateRenderingService.renderTemplateFromGithub("testRepository.java.ftl", repositoryContent, templateData);
        String repositoryPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/repository/TestRepository.java";
        addFileToZip(zipOutPut, repositoryPath, renderedRepository);
    }
    // Helper method to add Dockerfile
    private void addDockerFile(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String dockerContent = githubTemplateFetcher.fetchTemplate("spring-boot/Dockerfile.ftl");
        String renderedDocker = templateRenderingService.renderTemplateFromGithub("Dockerfile.ftl", dockerContent, templateData);
        String dockerPath = baseFolder + "Dockerfile";
        addFileToZip(zipOutPut, dockerPath, renderedDocker);
    }
    // Helper method to add application.properties file
    private void addApplicationProperties(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String propertiesContent = githubTemplateFetcher.fetchTemplate("spring-boot/src/main/resources/application.properties.ftl");
        String renderedProperties = templateRenderingService.renderTemplateFromGithub("application.properties.ftl", propertiesContent, templateData);
        String propertiesPath = baseFolder + "src/main/resources/application.properties";
        addFileToZip(zipOutPut, propertiesPath, renderedProperties);
    }
    private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        zipOutputStream.closeEntry();
    }
}
