package project.society.security.authorizedclient.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.util.Assert;
import project.society.data.dao.GenericReactiveDAO;
import project.society.data.dao.ReactiveDAOService;
import project.society.data.dto.HasId;
import project.society.security.session.model.ObjectToByteArrayAndBack;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class OAuth2ClientDAOService extends ReactiveDAOService<OAuth2AuthorizedClient, String> {
    public static final String ID_COLUMN_NAME = "id";
    public static final String CLIENT_ID_COLUMN_NAME = "client_id";

    private final Class<OAuth2AuthorizedClientProxy> dtoClass = OAuth2AuthorizedClientProxy.class;

    public OAuth2ClientDAOService(GenericReactiveDAO genericReactiveDAO) {
        super(genericReactiveDAO);
    }

    public Mono<OAuth2AuthorizedClient> getByClientIdAndPrincipalName(String clientId, String principalName) {
        return super.genericReactiveDAO.getR2dbcEntityTemplate()
                .selectOne(Query.query(Criteria.where(CLIENT_ID_COLUMN_NAME).is(clientId)
                        .and(Criteria.where(ID_COLUMN_NAME).is(principalName))), dtoClass)
                .map(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    public Mono<Integer> deleteByClientIdAndPrincipalName(String clientId, String principalName) {
        return super.genericReactiveDAO.getR2dbcEntityTemplate()
                .delete(Query.query(Criteria.where(CLIENT_ID_COLUMN_NAME).is(clientId)
                        .and(Criteria.where(ID_COLUMN_NAME).is(principalName)))
                , dtoClass);
    }

    @Override
    public Mono<Boolean> existsById(String s) {
        return super.genericReactiveDAO.existsById(s, dtoClass);
    }

    @Override
    public Mono<OAuth2AuthorizedClient> findOneById(String s) {
        return super.genericReactiveDAO.findOneById(s, dtoClass)
                .map(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    @Override
    public Flux<OAuth2AuthorizedClient> findById(String s) {
        return super.genericReactiveDAO.findById(s, dtoClass)
                .map(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    @Override
    public Mono<OAuth2AuthorizedClient> save(OAuth2AuthorizedClient item) {
        return super.genericReactiveDAO.save(new OAuth2AuthorizedClientProxy(item), dtoClass)
                .map(OAuth2AuthorizedClientProxy::getAuthorizedClient);
    }

    @Override
    public Mono<Integer> deleteById(String s) {
        return super.genericReactiveDAO.deleteById(s, dtoClass);
    }

    @Override
    public Flux<OAuth2AuthorizedClient> idLike(String idToMatch) {
        return null; // No use case.
    }

    @Table("authorized_clients")
    private static class OAuth2AuthorizedClientProxy implements HasId<String> {
        @Id @Column(ID_COLUMN_NAME) private final String id;
        @Column(CLIENT_ID_COLUMN_NAME) private final String clientRegistrationId;
        private final byte[] authorizedClient;

        public OAuth2AuthorizedClientProxy(OAuth2AuthorizedClient authorizedClient) {
            Assert.notNull(authorizedClient, "OAuth2AuthorizedClient cannot be null.");
            try {
                this.authorizedClient = ObjectToByteArrayAndBack.objectToByteArray(authorizedClient);
            } catch (IOException e) {
                throw new RuntimeException("Error serializing authorizedClient.");
            }
            this.id = authorizedClient.getPrincipalName();
            this.clientRegistrationId = authorizedClient.getClientRegistration().getClientId();
        }

        public String getClientRegistrationId() {
            return clientRegistrationId;
        }

        public String getId() {
            return id;
        }

        public OAuth2AuthorizedClient getAuthorizedClient() {
            try {
                return ObjectToByteArrayAndBack.byteArrayToObject(authorizedClient);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error deserializing authorizedClient.");
            }
        }
    }
}
