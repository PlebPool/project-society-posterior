package project.society.security.util;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
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
     * @param request Request containing principal containing access token.
     * @return {@link Mono} of {@link String}.
     */
    public Mono<String> extractAccessToken(ServerRequest request) {
        Mono<OAuth2AuthorizedClient> oAuth2AuthorizedClient = request.principal().filter(principal -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(oAuth2AuthenticationToken -> auth2AuthorizedClientRepository.loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken,
                        request.exchange()
                ));
        return oAuth2AuthorizedClient.map(oAuth2AuthorizedClient1 -> oAuth2AuthorizedClient1.getAccessToken().getTokenValue());
    }
}
