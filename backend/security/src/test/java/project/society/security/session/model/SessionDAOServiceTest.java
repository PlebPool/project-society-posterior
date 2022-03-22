package project.society.security.session.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import project.society.data.config.DataAutoConfig;
import project.society.security.session.config.SessionAutoConfig;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SessionAutoConfig.class, DataAutoConfig.class})
class SessionDAOServiceTest {

    public final String sessionId1 = "123";
    public final String sessionId2 = "321";

    private final Instant creationTime1 = Instant.now();
    private final Instant lastAccessedTime1 = Instant.now();

    private final CustomizedMapSession customizedMapSession1 = new CustomizedMapSession(
            this.sessionId1,
            this.sessionId1,
            Map.of("test-key1", "test-value1"),
            this.creationTime1,
            this.lastAccessedTime1
    );

    private final Instant creationTime2 = Instant.now().plus(Duration.ofSeconds(60));
    private final Instant lastAccessedTime2 = Instant.now().plus(Duration.ofSeconds(60));

    private final CustomizedMapSession customizedMapSession2 = new CustomizedMapSession(
            this.sessionId2,
            this.sessionId2,
            Map.of("test-key2", "test-value2"),
            this.creationTime2,
            this.lastAccessedTime2
    );

    @Autowired
    private SessionDAOService sessionDAOService;

    @Autowired
    private ApplicationContext context;

    @Test
    @Order(1)
    void save() {
        sessionDAOService.save(this.customizedMapSession1)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key1", "test-value1"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime1);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime1);
                    return Mono.empty();
                }).subscribe().dispose();

        sessionDAOService.save(this.customizedMapSession2)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key2", "test-value2"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime2);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime2);
                    return Mono.empty();
                }).subscribe().dispose();
    }

    @Test
    @Order(2)
    void existsById() {
        sessionDAOService.existsById(this.sessionId1).map(aBoolean -> Assertions.assertThat(aBoolean).isTrue()).subscribe().dispose();
        sessionDAOService.existsById(this.sessionId2).map(aBoolean -> Assertions.assertThat(aBoolean).isTrue()).subscribe().dispose();
        sessionDAOService.existsById("does not exist").map(aBoolean -> Assertions.assertThat(aBoolean).isFalse()).subscribe().dispose();
    }

    @Test
    @Order(3)
    void findOneById() {
        sessionDAOService.findOneById(this.sessionId1)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key1", "test-value1"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime1);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime1);
                    return Mono.empty();
                }).subscribe().dispose();

        sessionDAOService.findOneById(this.sessionId2)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key2", "test-value2"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime2);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime2);
                    return Mono.empty();
                }).subscribe().dispose();
    }

    @Test
    @Order(4)
    void findById() {
        sessionDAOService.findById(this.sessionId1)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId1);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key1", "test-value1"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime1);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime1);
                    return Mono.empty();
                }).subscribe().dispose();

        sessionDAOService.findById(this.sessionId2)
                .map(session -> {
                    Assertions.assertThat(session.getId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getOriginalId()).isEqualTo(this.sessionId2);
                    Assertions.assertThat(session.getSessionAttrs()).isEqualTo(Map.of("test-key2", "test-value2"));
                    Assertions.assertThat(session.getCreationTime()).isEqualTo(this.creationTime2);
                    Assertions.assertThat(session.getLastAccessedTime()).isEqualTo(this.creationTime2);
                    return Mono.empty();
                }).subscribe().dispose();
    }

    @Test
    @Order(5)
    void idLike() { // Has no use case here.
        sessionDAOService.idLike(this.sessionId1)
                .map(customizedMapSession -> {
                    Assertions.assertThat(customizedMapSession).isEqualTo(null);
                    return Mono.empty();
                }).subscribe().dispose();
    }

    @Test
    @Order(6)
    void deleteById() {
        sessionDAOService.deleteById(this.sessionId1).map(integer -> {
            Assertions.assertThat(integer).isEqualTo(1);
            return Mono.empty();
        }).subscribe().dispose();

        sessionDAOService.deleteById(this.sessionId2).map(integer -> {
            Assertions.assertThat(integer).isEqualTo(1);
            return Mono.empty();
        }).subscribe().dispose();

        sessionDAOService.deleteById("does not exist").map(integer -> {
            Assertions.assertThat(integer).isEqualTo(0);
            return Mono.empty();
        }).subscribe().dispose();
    }
}