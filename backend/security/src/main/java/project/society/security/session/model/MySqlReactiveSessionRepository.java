package project.society.security.session.model;

import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public class MySqlReactiveSessionRepository implements ReactiveSessionRepository<CustomizedMapSession> {

    /**
     * If non-null, this value is used to override
     * {@link Session#setMaxInactiveInterval(Duration)}.
     */
    private Integer defaultMaxInactiveInterval;

    //    private final Map<String, Session> sessions;
    private final SessionDAOService sessions;

    /**
     * Creates a new instance backed by the provided {@link Map}. This allows injecting a
     * distributed {@link Map}.
     * @param sessions the {@link Map} to use. Cannot be null.
     */
    public MySqlReactiveSessionRepository(SessionDAOService sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        }
        this.sessions = sessions;
    }

    /**
     * If non-null, this value is used to override
     * {@link Session#setMaxInactiveInterval(Duration)}.
     * @param defaultMaxInactiveInterval the number of seconds that the {@link Session}
     * should be kept alive between client requests.
     */
    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    @Override
    public Mono<Void> save(CustomizedMapSession session) {
        Mono<Integer> deleted = Mono.empty();
        if(!session.getId().equals(session.getOriginalId())) {
            deleted = sessions.deleteById(session.getOriginalId());
        }
        Mono<CustomizedMapSession> saveMono = sessions.save(session); // Updates if already exists.
        return deleted.then(saveMono).then();
    }

    @Override
    public Mono<CustomizedMapSession> findById(String id) {
        return sessions.findOneById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return sessions.deleteById(id).then();
    }

    @Override
    public Mono<CustomizedMapSession> createSession() {
        return Mono.defer(() -> {
            CustomizedMapSession result = new CustomizedMapSession();
            if (this.defaultMaxInactiveInterval != null) {
                result.setMaxInactiveInterval(Duration.ofSeconds(this.defaultMaxInactiveInterval));
            }
            return Mono.just(result);
        });
    }
}
