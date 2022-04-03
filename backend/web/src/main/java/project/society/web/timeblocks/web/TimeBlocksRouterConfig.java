package project.society.web.timeblocks.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class TimeBlocksRouterConfig {
    @Bean
    public RouterFunction<ServerResponse> timeBlockRouter(
            TimeBlockHandler timeBlockHandler
    ) {
        return RouterFunctions
                .route(GET("/timeBlocks/{uuid}"), timeBlockHandler::getTimeBlockById)
                .andRoute(GET("/dev/timeBlocks/init"), timeBlockHandler::initDb)
                .andRoute(GET("/timeBlocks"), timeBlockHandler::getTimeBlocksForUser)
                .andRoute(POST("/timeBlocks"), timeBlockHandler::postTimeBlockDay);
    }
}
