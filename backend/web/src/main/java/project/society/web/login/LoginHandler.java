package project.society.web.login;

import java.net.URI;
import java.util.Optional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.utility.PropertyNameHolder;
import reactor.core.publisher.Mono;

@Component
public class LoginHandler {
  private final String frontendUrl;

  public LoginHandler(Environment environment) {
    String url = environment.getProperty(PropertyNameHolder.PROJECT_DEV_FRONTEND_URL);
    Assert.notNull(url, PropertyNameHolder.PROJECT_DEV_FRONTEND_URL + " cannot be null.");
    this.frontendUrl = url;
  }

  /**
   * If redirect param contains frontend url, then we redirect to it. If not we redirect to home
   * page.
   *
   * @param request Current {@link ServerRequest}.
   * @return {@link Mono} of {@link ServerResponse}.
   */
  public Mono<ServerResponse> login(ServerRequest request) {
    String redirectUrl = this.frontendUrl;
    Optional<String> redirectParam = request.queryParam("redirect");
    if (redirectParam.isPresent()) {
      if (redirectParam.get().startsWith(frontendUrl)) {
        redirectUrl = redirectParam.get();
      }
    }
    return ServerResponse.temporaryRedirect(URI.create(redirectUrl)).build();
  }
}
