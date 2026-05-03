package ${basePackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * I love abood8
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Hello from your dynamically generated ${projectName}! and Also we LOVE ABOOD and AZOZZZZZZZ");
    }
}