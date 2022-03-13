package project.society.web.apis.google.classroom.classroom.courses.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class ClassroomCourseRouterConfig {
    @Bean
    public RouterFunction<ServerResponse> classroomCourseRouter(
            ClassroomCourseHandler classroomCourseHandler
    ) {
        return RouterFunctions
                .route(GET("/classroom/courses"), classroomCourseHandler::getAllCoursesForUser);
    }
}
