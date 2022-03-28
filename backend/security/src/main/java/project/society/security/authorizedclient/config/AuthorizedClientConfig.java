package project.society.security.authorizedclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import project.society.data.dao.GenericReactiveDAO;
import project.society.security.authorizedclient.MySqlReactiveOAuth2AuthorizedClientService;
import project.society.security.authorizedclient.model.OAuth2ClientDAOService;

@Configuration
public class AuthorizedClientConfig {

    private OAuth2ClientDAOService oAuth2ClientDAOService(GenericReactiveDAO dao) {
        return new OAuth2ClientDAOService(dao);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService serverOAuth2AuthorizedClientRepository(GenericReactiveDAO dao) {
        return new MySqlReactiveOAuth2AuthorizedClientService(this.oAuth2ClientDAOService(dao));
    }
}
