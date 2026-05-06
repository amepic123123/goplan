package ${basePackage}.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/${projectName}")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("${projectName} controller is running");
    }
}