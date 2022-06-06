package project.society.web.timeblocks.web;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TimeBlocksRouterConfig {
  @Bean
  public RouterFunction<ServerResponse> timeBlockRouter(TimeBlockHandler timeBlockHandler) {
    return RouterFunctions.route(GET("/timeBlocks/{uuid}"), timeBlockHandler::getTimeBlockById)
        .andRoute(GET("/dev/timeBlocks/init"), timeBlockHandler::initDb)
        .andRoute(GET("/timeBlocks"), timeBlockHandler::getTimeBlocksForUser)
        .andRoute(POST("/timeBlocks"), timeBlockHandler::postTimeBlockDay)
        .andRoute(PUT("/no-auth/timeBlocks/{uuid}"), timeBlockHandler::putTimeBlock);
  }
}
