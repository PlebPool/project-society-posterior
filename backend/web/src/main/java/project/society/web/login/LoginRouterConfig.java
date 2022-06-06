package project.society.web.login;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class LoginRouterConfig {
  @Bean
  public RouterFunction<ServerResponse> loginRouter(LoginHandler loginHandler) {
    return RouterFunctions.route(GET("/login/oauth2/google"), loginHandler::login);
  }
}
