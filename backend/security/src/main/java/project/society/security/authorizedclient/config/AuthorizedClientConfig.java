package project.society.security.authorizedclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import project.society.data.dao.GenericReactiveDAO;
import project.society.security.authorizedclient.MySqlReactiveOAuth2AuthorizedClientService;
import project.society.security.authorizedclient.model.OAuth2ClientDAOService;

@Configuration
public class AuthorizedClientConfig {

    @Bean
    public OAuth2ClientDAOService oAuth2ClientDAOService(GenericReactiveDAO grd, ReactiveClientRegistrationRepository rcrr) {
        return new OAuth2ClientDAOService(grd, rcrr);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService serverOAuth2AuthorizedClientRepository(OAuth2ClientDAOService ocds) {
        return new MySqlReactiveOAuth2AuthorizedClientService(ocds);
    }
}
