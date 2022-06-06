package project.society.web.google.classroom.courses.web;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CoursesRouterConfig {
  @Bean
  public RouterFunction<ServerResponse> coursesRouter(CoursesHandler coursesHandler) {
    return RouterFunctions.route(GET("/classroom/courses"), coursesHandler::getAllCoursesForUser);
  }
}
