package com.planagency.goplan.dto;

import java.util.List;

import com.planagency.goplan.enums.Frameworks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Map;
import jakarta.validation.constraints.Size;


import com.planagency.goplan.enums.Features;

public record ProjectRequestDto(

  @NotBlank(message = "Project name cannot be blank")
    @Size(max = 255)
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Project name can only contain lowercase letters, numbers, and hyphens")
    String projectName,

    @NotBlank(message = "Base package is required")
    @Pattern(regexp = "^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]$", message = "Must be a valid Java package name (e.g., com.goplan.api)")
    String basePackage,

    @NotNull(message = "Framework is required")
    Frameworks framework,

    @NotNull(message = "Features list cannot be null")
    List<Features> features,

    Map<String, Map<String, String>> models


) {}
