package project.society.security.authorizedclient;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import project.society.security.authorizedclient.model.OAuth2ClientDAOService;
import reactor.core.publisher.Mono;

public class MySqlReactiveOAuth2AuthorizedClientService implements ReactiveOAuth2AuthorizedClientService {
    private final OAuth2ClientDAOService oAuth2ClientDAOService;

    public MySqlReactiveOAuth2AuthorizedClientService(OAuth2ClientDAOService oAuth2ClientDAOService) {
        this.oAuth2ClientDAOService = oAuth2ClientDAOService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return (Mono<T>) this.oAuth2ClientDAOService.getByClientIdAndPrincipalName(clientRegistrationId, principalName);
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        return Mono.defer(() -> this
                .loadAuthorizedClient(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName())
                .map(oAuth2AuthorizedClient -> oAuth2ClientDAOService.save(oAuth2AuthorizedClient))
                .thenEmpty(Mono.empty()));

    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
        return this.oAuth2ClientDAOService.deleteByClientIdAndPrincipalName(clientRegistrationId, principalName)
                .then();
    }
}
