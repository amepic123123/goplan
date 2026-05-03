package com.planagency.goplan.strategy;

import com.planagency.goplan.enums.Frameworks;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface ProjectGeneratorStrategy {

    Frameworks getSupportedFramework();

    void generateProject(ZipOutputStream zipOutPut, ProjectRequestDto request) throws IOException;
    
}
