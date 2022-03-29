package project.society.security.authorizedclient.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import project.society.data.dao.GenericReactiveDAO;
import project.society.data.dao.ReactiveDAOService;
import project.society.data.dto.HasId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Set;

public class OAuth2ClientDAOService extends ReactiveDAOService<OAuth2AuthorizedClient, String> {
    public static final String PRINCIPAL_NAME_COLUMN = "principal_name";
    public static final String CLIENT_ID_COLUMN = "client_id";
    public static final String AUTHORIZED_CLIENT_BODY_COLUMN = "body";
    public static final String AUTHORIZED_CLIENTS_TABLE_NAME = "authorized_clients";
    public final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * {@link OAuth2AuthorizedClientProxy}.
     * This is a "transfer class". It's only job is to hold data before and after it's taken out of the database.
     * I needed to create this proxy class because
     * <ol>
     *     <li>I needed to add {@link Table}, {@link Id}, and {@link Column} annotations to the class.</li>
     *     <li>I didn't write {@link OAuth2AuthorizedClient}, and I want control over what goes in and out of the database.</li>
     *     <li>I needed to serialize and deserialize the actual {@link OAuth2AuthorizedClient}.</li>
     * </ol>
     */
    private final Class<OAuth2AuthorizedClientProxy> dtoClass = OAuth2AuthorizedClientProxy.class;

    protected final ReactiveClientRegistrationRepository registrationRepository;

    public OAuth2ClientDAOService(GenericReactiveDAO genericReactiveDAO, ReactiveClientRegistrationRepository registrationRepository) {
        super(genericReactiveDAO);
        this.registrationRepository = registrationRepository;
    }

    /**
     * Gets a row by clientId and principalName fields.
     * @param clientId {@link String}. Client id. ex "GOOGLE".
     * @param principalName {@link String} principal name ex "11110029835380".
     * @return {@link Mono} of {@link OAuth2AuthorizedClient}.
     */
    public Mono<OAuth2AuthorizedClient> getByClientIdAndPrincipalName(String clientId, String principalName) {
//        if(logger.isDebugEnabled()) {
//            logger.debug(String
//                    .format("%s: clientId=%s, principalName=%s", Object.class.getEnclosingMethod().getName(), clientId, principalName));
//        }
        return super.genericReactiveDAO.getR2dbcEntityTemplate()
                .selectOne(Query.query(Criteria.where(CLIENT_ID_COLUMN).is(clientId)
                        .and(Criteria.where(PRINCIPAL_NAME_COLUMN).is(principalName))), dtoClass)
                .flatMap(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    /**
     * Deletes a row by clientId and principalName fields.
     * @param clientId {@link String}. Client id. ex "GOOGLE".
     * @param principalName {@link String} principal name ex "11110029835380".
     * @return {@link Mono} of {@link OAuth2AuthorizedClient}.
     */
    public Mono<Integer> deleteByClientIdAndPrincipalName(String clientId, String principalName) {
        if(logger.isDebugEnabled()) {
            logger.debug(String
                    .format("%s: clientId=%s, principalName=%s", Object.class.getEnclosingMethod().getName(), clientId, principalName));
        }
        return super.genericReactiveDAO.getR2dbcEntityTemplate()
                .delete(Query.query(Criteria.where(CLIENT_ID_COLUMN).is(clientId)
                        .and(Criteria.where(PRINCIPAL_NAME_COLUMN).is(principalName)))
                , dtoClass);
    }

    /**
     * Turns the input {@link OAuth2AuthorizedClient} into an {@link OAuth2AuthorizedClientProxy}.
     * Which can be safely moved in and out of the database.
     * @param item Instance to save.
     * @return {@link OAuth2AuthorizedClient} (the saved instance returned).
     */
    @Override
    public Mono<OAuth2AuthorizedClient> save(OAuth2AuthorizedClient item) {
        if(logger.isDebugEnabled()) {
            logger.debug(String
                    .format("Saving: %s: %s=%s", Object.class.getEnclosingMethod().getName(), item.getClass().getName(), item));
        }
        return super.genericReactiveDAO.save(new OAuth2AuthorizedClientProxy(item), dtoClass)
                .flatMap(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    @Table(AUTHORIZED_CLIENTS_TABLE_NAME)
    private class OAuth2AuthorizedClientProxy implements HasId<String> {
        @Id @Column(PRINCIPAL_NAME_COLUMN) private final String principalName;
        @Column(CLIENT_ID_COLUMN) private final String clientRegistrationId;
        private final String accessTokenType;
        private final String accessTokenValue;
        private String accessTokenIssuedAt;
        private String accessTokenExpiresAt;
        private final String accessTokenScopes;
        private String refreshTokenValue;
        private String refreshTokenIssuedAt;

        @Override
        public String getId() {
            return this.principalName;
        }

        @SuppressWarnings("unused")
        public String getRefreshTokenValue() {
            return refreshTokenValue;
        }

        @SuppressWarnings("unused")
        public String getRefreshTokenIssuedAt() {
            return refreshTokenIssuedAt;
        }

        public OAuth2AuthorizedClientProxy(OAuth2AuthorizedClient authorizedClient) {
            Assert.notNull(authorizedClient, "OAuth2AuthorizedClient cannot be null.");
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            this.clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
            this.principalName = authorizedClient.getPrincipalName();
            this.accessTokenType = accessToken.getTokenType().getValue();
            this.accessTokenValue = accessToken.getTokenValue();
            if (accessToken.getIssuedAt() != null) {
                this.accessTokenIssuedAt = LocalDateTime.ofInstant(accessToken.getIssuedAt(), ZoneId.systemDefault()).toString();
            }
            if (accessToken.getExpiresAt() != null) {
                this.accessTokenExpiresAt = accessToken.getExpiresAt().toString();
            }
            this.accessTokenScopes = StringUtils.collectionToDelimitedString(accessToken.getScopes(), ",");
            OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
            if (refreshToken != null) {
                this.refreshTokenValue = refreshToken.getTokenValue();
            }
            if (refreshToken != null) {
                if(refreshToken.getIssuedAt() != null) {
                    this.refreshTokenIssuedAt = LocalDateTime.ofInstant(refreshToken.getIssuedAt(), ZoneId.systemDefault()).toString();
                }
            }
        }

        public Mono<OAuth2AuthorizedClient> getAuthorizedClient() {
            return registrationRepository.findByRegistrationId(this.clientRegistrationId)
                    .map(clientRegistration -> new OAuth2AuthorizedClient(clientRegistration, this.principalName, this.getAccessToken()));
        }

        public OAuth2AccessToken getAccessToken() {
            OAuth2AccessToken.TokenType tokenType = null;
            if(OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(this.accessTokenType)) {
                tokenType = OAuth2AccessToken.TokenType.BEARER;
            }
            Set<String> scopes = Collections.emptySet();
            if(!accessTokenScopes.isEmpty()) {
                scopes = Set.of(this.accessTokenScopes.split(","));
            }
            assert tokenType != null;
            return new OAuth2AccessToken(tokenType, this.accessTokenValue,
                    Instant.parse(this.accessTokenIssuedAt), Instant.parse(this.accessTokenExpiresAt), scopes);
        }
    }

    /**
     * @deprecated No real use case in this context.
     */
    @Deprecated
    @Override
    public Mono<Boolean> existsById(String s) {
        return null;
    }

    /**
     * @deprecated No real use case in this context.
     */
    @Deprecated
    @Override
    public Mono<OAuth2AuthorizedClient> findOneById(String s) {
        return null;
    }

    /**
     * @deprecated No real use case in this context.
     */
    @Deprecated
    @Override
    public Flux<OAuth2AuthorizedClient> findById(String s) {
        return null;
    }

    /**
     * @deprecated No real use case in this context.
     */
    @Deprecated
    @Override
    public Mono<Integer> deleteById(String s) {
        return null;
    }

    /**
     * @deprecated No real use case in this context.
     */
    @Deprecated
    @Override
    public Flux<OAuth2AuthorizedClient> idLike(String idToMatch) {
        return null;
    }
}
