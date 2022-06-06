package project.society.application;

import java.time.Instant;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "project.society")
public class Application {
  public static void main(String[] args) {
    Logger.getLogger("app").info(Instant.now().toString());
    SpringApplication.run(Application.class, args);
  }
}
