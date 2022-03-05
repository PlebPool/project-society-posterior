package project.society.data.dao;

import project.society.data.dto.HasId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ReactiveDAOService<OUT extends HasId<ID>, ID> {
    protected final GenericReactiveDAO genericReactiveDAO;

    public ReactiveDAOService(GenericReactiveDAO genericReactiveDAO) {
        this.genericReactiveDAO = genericReactiveDAO;
    }

    public abstract Mono<Boolean> existsById(ID id);
    public abstract Mono<OUT> findOneById(ID id);
    public abstract Flux<OUT> findById(ID id);
    public abstract Mono<OUT> save(OUT item);
    public abstract Mono<Integer> deleteById(ID id);
    public abstract Flux<OUT> idLike(String idToMatch);
}
