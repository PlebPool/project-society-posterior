package project.society.web.apis.google.classroom.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClassroomWebClientConfig {
    private static final String GOOGLE_CLASSROOM_SERVICE_BASE_URL = "https://classroom.googleapis.com/v1";
    @Bean("classroomWebClient")
    public WebClient classroomClient() {
        return WebClient.builder().baseUrl(GOOGLE_CLASSROOM_SERVICE_BASE_URL).build();
    }
}
