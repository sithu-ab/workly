package app.demo.workly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WorklyApplication {

    @GetMapping("/")
    String home() {
        return "Hello Sprint Boot!";
    }

    public static void main(String[] args) {
        SpringApplication.run(WorklyApplication.class, args);
    }

}
