package project.society.security.session.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import project.society.data.dao.GenericReactiveDAO;
import project.society.data.dao.ReactiveDAOService;
import project.society.data.dto.HasId;
import project.society.security.session.CustomizedMapSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;

public class SessionDAOService extends ReactiveDAOService<CustomizedMapSession, String> {
    Class<DbMapSession> dtoClazz = DbMapSession.class;
    public SessionDAOService(GenericReactiveDAO genericReactiveDAO) {
        super(genericReactiveDAO);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return super.genericReactiveDAO.existsById(id, dtoClazz);
    }

    @Override
    public Mono<CustomizedMapSession> findOneById(String id) {
        return super.genericReactiveDAO.findOneById(id, dtoClazz).mapNotNull(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Flux<CustomizedMapSession> findById(String id) {
        return super.genericReactiveDAO.findById(id, dtoClazz).mapNotNull(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Mono<CustomizedMapSession> save(CustomizedMapSession item) {
        return super.genericReactiveDAO.save(new DbMapSession(item), dtoClazz).mapNotNull(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return super.genericReactiveDAO.deleteById(id, dtoClazz);
    }

    @Override
    public Flux<CustomizedMapSession> idLike(String idToMatch) {
        return super.genericReactiveDAO.idLike(idToMatch, dtoClazz).mapNotNull(DbMapSession::getAsCustomizedMapSession);
    }

    @Table("sessions")
    private static class DbMapSession implements HasId<String> {

        @Id
        @Column("id") private String id;

        private String originalId;

        private byte[] sessionAttrs;

        private Instant creationTime;

        private Instant lastAccessedTime;

        /**
         * Based on {@link CustomizedMapSession#getMaxInactiveInterval()}
         */
        private Instant expiresAt;

        /**
         * This class is a sql friendly version of {@link CustomizedMapSession}
         * @param session {@link CustomizedMapSession}
         */
        public DbMapSession(CustomizedMapSession session) {
            this.id = session.getId();
            this.originalId = session.getId();
            try {
                this.sessionAttrs = ObjectToByteArrayAndBack.objectToByteArray((Serializable) session.getSessionAttrs());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.creationTime = session.getCreationTime();
            this.lastAccessedTime = session.getLastAccessedTime();
            this.expiresAt = session.getLastAccessedTime().plus(session.getMaxInactiveInterval());
        }

        /**
         * Empty constructor needed for object mapper.
         */
        @SuppressWarnings("unused")
        public DbMapSession() { }

        /**
         * Turns the current instance of {@link DbMapSession} into a {@link CustomizedMapSession}.
         * @return {@link CustomizedMapSession} representation of this.
         */
        public CustomizedMapSession getAsCustomizedMapSession() {
            if(!Instant.now().isAfter(this.expiresAt)) {
                try {
                    return new CustomizedMapSession(
                            this.id,
                            this.originalId,
                            ObjectToByteArrayAndBack.byteArrayToObject(sessionAttrs),
                            creationTime,
                            Instant.now()
                    );
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String getId() {
            return id;
        }

        public String getOriginalId() {
            return originalId;
        }

        public void setOriginalId(String originalId) {
            this.originalId = originalId;
        }

        public byte[] getSessionAttrs() {
            return sessionAttrs;
        }

        public void setSessionAttrs(byte[] sessionAttrs) {
            this.sessionAttrs = sessionAttrs;
        }

        public Instant getCreationTime() {
            return this.creationTime;
        }

        public void setCreationTime(Instant creationTime) {
            this.creationTime = creationTime;
        }

        public Instant getLastAccessedTime() {
            return this.lastAccessedTime;
        }

        public void setLastAccessedTime(Instant lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }

        public Instant getExpiresAt() {
            return this.expiresAt;
        }

        public void setExpiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
        }
    }

}
