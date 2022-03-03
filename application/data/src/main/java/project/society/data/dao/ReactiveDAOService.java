package project.society.data.dao;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

abstract class ReactiveDAOService<T, ID> {
    protected final GenericReactiveDAO genericReactiveDAO;

    public ReactiveDAOService(GenericReactiveDAO genericReactiveDAO) {
        this.genericReactiveDAO = genericReactiveDAO;
    }

    abstract Mono<Boolean> existsById(ID id);
    abstract Flux<T> findOneById(ID id);
    abstract Mono<T> findById(ID id);
    abstract Mono<T> save(T item);
    abstract Mono<Integer> deleteById(ID id);
    abstract Flux<T> idLike(String idToMatch);
}
