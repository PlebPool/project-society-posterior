package project.society.web.mock;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MockRouterConfig {
  /**
   * Router matching a path to a {@link
   * org.springframework.web.reactive.function.server.HandlerFunction}.
   *
   * @param mockHandler {@link Bean}
   * @return {@link RouterFunction}.
   */
  @Bean
  public RouterFunction<ServerResponse> mockRouter(MockHandler mockHandler) {
    return RouterFunctions.route(GET("/no-auth/mock"), mockHandler::mock);
  }
}
