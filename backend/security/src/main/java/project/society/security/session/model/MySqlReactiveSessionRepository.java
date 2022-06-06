package project.society.security.session.model;

import java.time.Duration;
import java.util.Map;
import org.springframework.core.env.Environment;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import project.society.utility.PropertyNameHolder;
import reactor.core.publisher.Mono;

public class MySqlReactiveSessionRepository
    implements ReactiveSessionRepository<CustomizedMapSession> {

  /**
   * If non-null, this value is used to override {@link Session#setMaxInactiveInterval(Duration)}.
   */
  private Integer maxInactiveInterval;

  private final SessionDAOService sessions;

  /**
   * Creates a new instance backed by the provided {@link
   * project.society.data.dao.ReactiveDAOService}. If environment variable with the name of string
   * value of {@link PropertyNameHolder#PROJECT_DEV_SESSION_INACTIVE_INTERVAL} is set. It will
   * override {@link Session#setMaxInactiveInterval(Duration)}
   *
   * @param sessions the {@link Map} to use. Cannot be null.
   */
  public MySqlReactiveSessionRepository(SessionDAOService sessions, Environment env) {
    String maxInactiveIntervalEnv =
        env.getProperty(PropertyNameHolder.PROJECT_DEV_SESSION_INACTIVE_INTERVAL);
    if (maxInactiveIntervalEnv != null) {
      this.maxInactiveInterval = Integer.valueOf(maxInactiveIntervalEnv);
    }
    if (sessions == null) {
      throw new IllegalArgumentException("sessions cannot be null");
    }
    this.sessions = sessions;
  }

  /**
   * If non-null, this value is used to override {@link Session#setMaxInactiveInterval(Duration)}.
   *
   * @param maxInactiveInterval the number of seconds that the {@link Session} should be kept alive
   *     between client requests.
   */
  public void setMaxInactiveInterval(int maxInactiveInterval) {
    this.maxInactiveInterval = maxInactiveInterval;
  }

  @Override
  public Mono<Void> save(CustomizedMapSession session) {
    Mono<Integer> deleted = Mono.empty();
    if (!session.getId().equals(session.getOriginalId())) {
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
    return Mono.defer(
        () -> {
          CustomizedMapSession result = new CustomizedMapSession();
          if (this.maxInactiveInterval != null) {
            result.setMaxInactiveInterval(Duration.ofSeconds(this.maxInactiveInterval));
          }
          return Mono.just(result);
        });
  }
}
