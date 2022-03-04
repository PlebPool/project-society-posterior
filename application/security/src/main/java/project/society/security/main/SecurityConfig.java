package project.society.security.main;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import project.society.application.config.PropertyNameHolder;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    public static final String GOOGLE_ENV_PATH = "spring.security.oauth2.client.registration.google";
    private final Environment environment;

    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            @Qualifier("myCorsConfig") CorsConfigurationSource corsSource
    ) {
        return http
                .cors().configurationSource(corsSource)
                .and().authorizeExchange()
                        .pathMatchers("/no-auth/**", "/").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyExchange().authenticated()
                .and().oauth2Login()
                .and().oauth2Client().clientRegistrationRepository(this.reactiveClientRegistrationRepository())
                .and().build();
    }

    @Bean
    @ConditionalOnProperty(value = PropertyNameHolder.PROJECT_DEV_CSRF, havingValue = "false")
    public SecurityWebFilterChain csrfDisable(ServerHttpSecurity security) {
        return security.csrf().disable().build();
    }

    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(environment.getProperty(GOOGLE_ENV_PATH + ".client-id"))
                .clientSecret(environment.getProperty(GOOGLE_ENV_PATH + ".client-secret"))
                .scope("openid", "profile", "email", "address", "phone",
                        "https://www.googleapis.com/auth/classroom.courses.readonly",
                        "https://www.googleapis.com/auth/classroom.coursework.me.readonly")
                .redirectUri(environment.getProperty(GOOGLE_ENV_PATH + ".redirect-uri"))
                .build();
    }

    /**
     * @param clientRegistrationRepository {@link ReactiveClientRegistrationRepository}
     * @param authorizedClientRepository {@link ServerOAuth2AuthorizedClientRepository}
     * @return - {@link ReactiveOAuth2AuthorizedClientManager}
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .clientCredentials()
                .password()
                .build();
        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }
}
