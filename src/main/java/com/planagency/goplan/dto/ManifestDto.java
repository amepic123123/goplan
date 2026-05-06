package com.planagency.goplan.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManifestDto {
    private String repository;
    private String description;
    private String timestamp;
    private List<TemplateFileDto> files;
}
