package project.society.security.session.model;

import project.society.data.dao.GenericReactiveDAO;
import project.society.data.dao.ReactiveDAOService;
import project.society.security.session.CustomizedMapSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return super.genericReactiveDAO.findOneById(id, dtoClazz).map(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Flux<CustomizedMapSession> findById(String id) {
        return super.genericReactiveDAO.findById(id, dtoClazz).map(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Mono<CustomizedMapSession> save(CustomizedMapSession item) {
        return super.genericReactiveDAO.save(new DbMapSession(item), dtoClazz).map(DbMapSession::getAsCustomizedMapSession);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return super.genericReactiveDAO.deleteById(id, dtoClazz);
    }

    @Override
    public Flux<CustomizedMapSession> idLike(String idToMatch) {
        return super.genericReactiveDAO.idLike(idToMatch, dtoClazz).map(DbMapSession::getAsCustomizedMapSession);
    }
}
