package project.society.web.google.classroom.progress.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class CourseProgressRouterConfig {
    @Bean
    public RouterFunction<ServerResponse> courseProgressRouter(
            CourseProgressHandler courseProgressHandler
    ) {
        return RouterFunctions.route(GET("/classroom/courses/{courseId}/progress"), courseProgressHandler::getCourseProgressById)
                .andRoute(GET("/classroom/courses/progress"), courseProgressHandler::getCourseProgressForUser);
    }
}
