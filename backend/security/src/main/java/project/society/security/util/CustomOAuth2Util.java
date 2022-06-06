package project.society.security.util;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
public class CustomOAuth2Util {
  private final ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository;

  public CustomOAuth2Util(ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository) {
    this.auth2AuthorizedClientRepository = auth2AuthorizedClientRepository;
  }

  /**
   * Extracts access token from request.
   *
   * @param request Request containing principal containing access token.
   * @return {@link Mono} of {@link String}.
   */
  public Mono<String> extractAccessToken(ServerRequest request) {
    return this.extractAuthorizedClient(request)
        .map(OAuth2AuthorizedClient::getAccessToken)
        .map(OAuth2AccessToken::getTokenValue);
  }

  public Mono<String> extractPrincipalName(ServerRequest request) {
    return this.extractAuthorizedClient(request).map(OAuth2AuthorizedClient::getPrincipalName);
  }

  private Mono<OAuth2AuthorizedClient> extractAuthorizedClient(ServerRequest request) {
    return request
        .principal()
        .filter(principal -> principal instanceof OAuth2AuthenticationToken)
        .cast(OAuth2AuthenticationToken.class)
        .flatMap(
            oAuth2AuthenticationToken ->
                auth2AuthorizedClientRepository.loadAuthorizedClient(
                    oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                    oAuth2AuthenticationToken,
                    request.exchange()));
  }
}
