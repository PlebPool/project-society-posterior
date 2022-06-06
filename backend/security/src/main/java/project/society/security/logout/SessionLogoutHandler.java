package project.society.security.logout;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Component
public class SessionLogoutHandler implements ServerLogoutHandler {

  @Override
  public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
    return exchange.getExchange().getSession().flatMap(WebSession::invalidate).then();
  }
}
