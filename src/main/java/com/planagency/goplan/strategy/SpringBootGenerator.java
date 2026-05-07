package com.planagency.goplan.strategy;

import com.planagency.goplan.enums.*;
import org.springframework.stereotype.Component;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import com.planagency.goplan.service.TemplateRenderingService;
import com.planagency.mapping.ValidationMapper;

import java.util.zip.ZipEntry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.planagency.goplan.service.GithubTemplateFetcher;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

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
        boolean hasModels = request.features().contains(Features.MODELS)
            && request.models() != null
            && !request.models().isEmpty();

        Map<String, Object> commonTemplateData = Map.of(
            "projectName", request.projectName(),
            "basePackage", request.basePackage(),
            "serverPort", "8080"
        );

        createDirectoryStructure(zipOutPut, baseFolder);
        addCoreFiles(zipOutPut, baseFolder, commonTemplateData, request.basePackage());
        addPomFile(zipOutPut, baseFolder, commonTemplateData);
        addMavenWrapper(zipOutPut, baseFolder);
        addApplicationProperties(zipOutPut, baseFolder, commonTemplateData);

        if(request.features().contains(Features.CONTROLLERS) && !hasModels){
            addControllerFiles(zipOutPut, baseFolder, commonTemplateData, request.basePackage());
        }
        if(request.features().contains(Features.SERVICES) && !hasModels){
            addServiceFiles(zipOutPut, baseFolder, commonTemplateData, request.basePackage());
        }
        if(request.features().contains(Features.REPOSITORIES) && !hasModels){
            addRepositoryFiles(zipOutPut, baseFolder, commonTemplateData, request.basePackage());
        }
        if(request.features().contains(Features.DOCKER)){
            addDockerFile(zipOutPut, baseFolder, commonTemplateData);
        }
        if(hasModels){
            for (var entry : request.models().entrySet()) {
                Map<String, Object> modelTemplateData = buildModelTemplateData(request, entry.getKey(), entry.getValue());
                addModelFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                addDtoFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                if(request.features().contains(Features.REPOSITORIES)) {
                    addRepositoryFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                }
                if(request.features().contains(Features.SERVICES)) {
                    addServiceFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                }
                addMapperFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                if(request.features().contains(Features.CONTROLLERS)) {
                    addControllerFiles(zipOutPut, baseFolder, modelTemplateData, request.basePackage());
                }
            }
        }
    }

    private void addDtoFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String dtoContent = githubTemplateFetcher.fetchTemplate("dto/testDto.java.ftl");
        String renderedDto = templateRenderingService.renderTemplateFromGithub("testDto.java.ftl", dtoContent, templateData);
        String dtoPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/dto/" + templateData.get("modelName") + "Dto.java";
        addFileToZip(zipOutPut, dtoPath, renderedDto);
    }

    private Map<String, Object> buildModelTemplateData(ProjectRequestDto request, String modelName, Map<String, String> props) {
        Map<String, String> safeProps = props != null ? props : Map.of();

        List<Map<String, String>> properties = safeProps.entrySet().stream()
            .map(e -> {
                String name = e.getKey();
                String userType = e.getValue();
                String javaType = ValidationMapper.mapType(userType);
                String validation = ValidationMapper.mapValidation(name);
                Map<String, String> field = new HashMap<>();
                field.put("name", name);
                field.put("fieldName", name);
                field.put("type", javaType);
                field.put("fieldType", javaType);
                field.put("javaType", javaType);
                field.put("validation", validation);
                return field;
            })
            .collect(Collectors.toList());

        Set<String> imports = new HashSet<>();
        for (var p : properties) {
            if ("LocalDateTime".equals(p.get("type"))) {
                imports.add("java.time.LocalDateTime");
            }
            String validation = p.get("validation");
            if (validation.contains("@Email")) imports.add("jakarta.validation.constraints.Email");
            if (validation.contains("@NotBlank")) imports.add("jakarta.validation.constraints.NotBlank");
            if (validation.contains("@Pattern")) imports.add("jakarta.validation.constraints.Pattern");
            if (validation.contains("@Min")) imports.add("jakarta.validation.constraints.Min");
            if (validation.contains("@Max")) imports.add("jakarta.validation.constraints.Max");
        }

        Map<String, Object> modelTemplateData = new HashMap<>();
        modelTemplateData.put("projectName", request.projectName());
        modelTemplateData.put("basePackage", request.basePackage());
        modelTemplateData.put("serverPort", "8080");
        modelTemplateData.put("modelName", modelName);
        modelTemplateData.put("properties", properties);
        modelTemplateData.put("fields", properties);
        modelTemplateData.put("attributes", properties);
        modelTemplateData.put("imports", imports);

        return modelTemplateData;
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
        String mvnwWrapperContent = githubTemplateFetcher.fetchTemplate(".mvn/wrapper/maven-wrapper.properties.ftl");
        String renderedMvnwWrapper = templateRenderingService.renderTemplateFromGithub("maven-wrapper.properties", mvnwWrapperContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + ".mvn/wrapper/maven-wrapper.properties", renderedMvnwWrapper);
        
        String mvnwContent = githubTemplateFetcher.fetchTemplate("mvnw.ftl");
        String renderedMvnw = templateRenderingService.renderTemplateFromGithub("mvnw", mvnwContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + "mvnw", renderedMvnw);
        String mvnwCmdContent = githubTemplateFetcher.fetchTemplate("mvnw.cmd.ftl");
        String renderedMvnwCmd = templateRenderingService.renderTemplateFromGithub("mvnw.cmd", mvnwCmdContent, Map.of());
        addFileToZip(zipOutPut, baseFolder + "mvnw.cmd", renderedMvnwCmd);
    }
    // Helper method to add core application file
    private void addCoreFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String coreFileContent = githubTemplateFetcher.fetchTemplate("Application.java.ftl");
        String renderedCoreFile = templateRenderingService.renderTemplateFromGithub("Application.java.ftl", coreFileContent, templateData);
        String packagePath = basePackage.replace('.', '/') + "/";
        String coreFilePath = baseFolder + "src/main/java/" + packagePath + "Application.java";
        addFileToZip(zipOutPut, coreFilePath, renderedCoreFile);
    }
    // Helper method to add POM file
    private void addPomFile(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String pomContent = githubTemplateFetcher.fetchTemplate("pom.xml.ftl");
        String renderedPom = templateRenderingService.renderTemplateFromGithub("pom.xml.ftl", pomContent, templateData);
        String pomFilePath = baseFolder + "pom.xml";
        addFileToZip(zipOutPut, pomFilePath, renderedPom);
    }
    // Helper method to add controller file
    private void addControllerFiles(ZipOutputStream zipOutPut,String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
    Map<String, Object> controllerTemplateData = new HashMap<>(templateData);
    String modelName = templateData.containsKey("modelName") ? String.valueOf(templateData.get("modelName")) : "Test";
    controllerTemplateData.put("modelName", modelName);
    controllerTemplateData.put("modelSpecific", templateData.containsKey("modelName"));
     String controllerContent = githubTemplateFetcher.fetchTemplate("controller/testController.java.ftl");
     String renderedController = templateRenderingService.renderTemplateFromGithub("testController.java.ftl", controllerContent, controllerTemplateData);
     String controllerPath =  baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/controller/" + modelName + "Controller.java";
            addFileToZip(zipOutPut, controllerPath, renderedController);
    }
    // Helper method to add service file
    private void addServiceFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String serviceContent = githubTemplateFetcher.fetchTemplate("service/testService.java.ftl");
        Map<String, Object> serviceTemplateData = new HashMap<>(templateData);
        String serviceName = templateData.containsKey("modelName") ? String.valueOf(templateData.get("modelName")) : "Test";
        serviceTemplateData.put("serviceName", serviceName);
        serviceTemplateData.put("modelSpecific", templateData.containsKey("modelName"));
        String renderedService = templateRenderingService.renderTemplateFromGithub("testService.java.ftl", serviceContent, serviceTemplateData);
        String servicePath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/service/" + serviceName + "Service.java";
        addFileToZip(zipOutPut, servicePath, renderedService);
    }
    // Helper method to add model file
    private void addModelFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String modelContent = githubTemplateFetcher.fetchTemplate("model/testModel.java.ftl");
        String renderedModel = templateRenderingService.renderTemplateFromGithub("testModel.java.ftl", modelContent, templateData);
        String modelName = String.valueOf(templateData.get("modelName"));
        String modelPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/model/" + modelName + ".java";
        addFileToZip(zipOutPut, modelPath, renderedModel);
    }
    // Helper method to add repository file
    private void addRepositoryFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String repositoryContent = githubTemplateFetcher.fetchTemplate("repository/testRepository.java.ftl");
        Map<String, Object> repositoryTemplateData = new HashMap<>(templateData);
        String repositoryName = templateData.containsKey("modelName") ? String.valueOf(templateData.get("modelName")) : "Test";
        repositoryTemplateData.put("repositoryName", repositoryName);
        repositoryTemplateData.put("modelSpecific", templateData.containsKey("modelName"));
        String renderedRepository = templateRenderingService.renderTemplateFromGithub("testRepository.java.ftl", repositoryContent, repositoryTemplateData);
        String repositoryPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/repository/" + repositoryName + "Repository.java";
        addFileToZip(zipOutPut, repositoryPath, renderedRepository);
    }

    private void addMapperFiles(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData, String basePackage) throws IOException {
        String mapperContent = githubTemplateFetcher.fetchTemplate("mapper/testMapper.java.ftl");
        String renderedMapper = templateRenderingService.renderTemplateFromGithub("testMapper.java.ftl", mapperContent, templateData);
        String modelName = String.valueOf(templateData.get("modelName"));
        String mapperPath = baseFolder + "src/main/java/" + basePackage.replace('.', '/') + "/mapper/" + modelName + "Mapper.java";
        addFileToZip(zipOutPut, mapperPath, renderedMapper);
    }
    // Helper method to add Dockerfile
    private void addDockerFile(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String dockerContent = githubTemplateFetcher.fetchTemplate("Dockerfile.ftl");
        String renderedDocker = templateRenderingService.renderTemplateFromGithub("Dockerfile.ftl", dockerContent, templateData);
        String dockerPath = baseFolder + "Dockerfile";
        addFileToZip(zipOutPut, dockerPath, renderedDocker);
    }
    // Helper method to add application.properties file
    private void addApplicationProperties(ZipOutputStream zipOutPut, String baseFolder, Map<String, Object> templateData) throws IOException {
        String propertiesContent = githubTemplateFetcher.fetchTemplate("src/main/resources/application.properties.ftl");
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


