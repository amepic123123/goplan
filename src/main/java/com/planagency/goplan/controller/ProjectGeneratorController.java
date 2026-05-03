package com.planagency.goplan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.planagency.goplan.service.ProjectGeneratorService;

import jakarta.validation.Valid;

import com.planagency.goplan.dto.ProjectRequestDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectGeneratorController {
    static final String BASE_URL = "/api/v1/projects";

    ProjectGeneratorService projectGeneratorService;

    public ProjectGeneratorController(ProjectGeneratorService projectGeneratorService) {
        this.projectGeneratorService = projectGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProject(@Valid @RequestBody ProjectRequestDto request) {
        byte[] projectZip = projectGeneratorService.generateProject(request);
       return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.projectName() + ".zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(projectZip);
    }
}
