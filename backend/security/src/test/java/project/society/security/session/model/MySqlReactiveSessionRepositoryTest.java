//package project.society.security.session.model;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
//import org.springframework.session.Session;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import project.society.security.session.SecurityTestApplication;
//import project.society.security.session.config.TestConfig;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//
///**
// * This test class tells us whether {@link MySqlReactiveSessionRepository} works.
// * It also indirectly tells us that {@link org.springframework.data.r2dbc.core.R2dbcEntityTemplate},
// * {@link project.society.data.dao.GenericReactiveDAO}, and {@link project.society.data.dao.ReactiveDAOService} works.
// * And that the database is set up properly.
// * As all of these are essential to the persistence of the sessions.
// */
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {TestConfig.class, SecurityTestApplication.class})
//@DataR2dbcTest
//class MySqlReactiveSessionRepositoryTest {
//    @Autowired private MySqlReactiveSessionRepository mySqlReactiveSessionRepository;
//    String sessionId;
//
//    @BeforeEach
//    void setUp() { // This also serves to test .createSession();
//        this.mySqlReactiveSessionRepository.setMaxInactiveInterval(420);
//        Mono<CustomizedMapSession> sessionMono = this.mySqlReactiveSessionRepository.createSession();
//        sessionMono.map(session -> {
//            // Testing functionality.
//            this.testSession(session);
//            // Saving session.
//            return this.mySqlReactiveSessionRepository.save(session);
//        }).subscribe().dispose();
//    }
//
//    @Test
//    @Order(1)
//    void findById() {
//        // Getting session and testing it.
//        this.mySqlReactiveSessionRepository.findById(this.sessionId).map(session -> {
//            // Checking if it's valid. Testing persistence of attributes/fields.
//            this.testSession(session);
//            return Mono.empty();
//        }).subscribe().dispose();
//    }
//
//    @Test
//    @Order(2)
//    void deleteById() {
//        this.mySqlReactiveSessionRepository.deleteById(this.sessionId).subscribe().dispose();
//        this.mySqlReactiveSessionRepository.findById(this.sessionId)
//                .map(it -> {
//                    Assertions.fail("Found: " + it);
//                    return Mono.empty();
//                })
//                .subscribe().dispose();
//
//    }
//
//    private void testSession(CustomizedMapSession session) {
//        // Validating that maxInactiveInterval is set and in effect.
//        Assertions.assertThat(session.getMaxInactiveInterval()).isEqualTo(Duration.ofSeconds(420));
//        // Testing Id setting.
//        this.sessionId = session.getId();
//        Assertions.assertThat(session.getId()).isEqualTo(session.getOriginalId());
//        // Testing if not expired at creation.
//        Assertions.assertThat(session.isExpired()).isFalse();
//        // Testing setting of attributes if not set already.
//        // If "test" is set, this turns into a test of attribute persistence.
//        // If "test" is set, it means the session comes from the database.
//        if(session.getAttribute("test") == null) {
//            this.addTestAttribute(session);
//        }
//        // Testing removing of attributes.
//        session.removeAttribute("test");
//        Assertions.assertThat(session.getAttributeNames()).doesNotContain("test");
//        // Adding attribute for possible persistence test.
//        this.addTestAttribute(session);
//    }
//
//    private void addTestAttribute(Session session) {
//        session.setAttribute("test", "test-value");
//        Assertions.assertThat(session.getAttributeNames()).contains("test");
//        Assertions.assertThat((String) session.getAttribute("test")).isEqualTo("test-value");
//    }
//}