package com.planagency.goplan.startegy;

import com.planagency.goplan.enums.*;
import org.springframework.stereotype.Component;
import com.planagency.goplan.dto.ProjectRequestDto;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

@Component
public class SpringBootGenerator implements ProjectGeneratorStrategy {


    @Override
    public Frameworks getSupportedFramework() {
        return Frameworks.SPRING_BOOT;
    }

    @Override
    public void generateProject(ZipOutputStream zipOutPut, ProjectRequestDto request) throws IOException {
       
    }
    
    private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String content) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(content.getBytes());
        zipOutputStream.closeEntry();
    }
}
