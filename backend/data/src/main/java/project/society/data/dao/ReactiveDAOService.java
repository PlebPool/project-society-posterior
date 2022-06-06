package project.society.data.dao;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ReactiveDAOService<OUT, ID> {
  protected final GenericReactiveDAO genericReactiveDAO;

  public ReactiveDAOService(GenericReactiveDAO genericReactiveDAO) {
    this.genericReactiveDAO = genericReactiveDAO;
  }

  /**
   * Checks if row with id exists.
   *
   * @param id Target id.
   * @return {@link Mono} of {@link Boolean}.
   */
  public abstract Mono<Boolean> existsById(ID id);

  /**
   * Finds one row with id.
   *
   * @param id Target id.
   * @return {@link Mono} of {@link OUT}.
   */
  public abstract Mono<OUT> findOneById(ID id);

  /**
   * Finds one or more rows with id.
   *
   * @param id Target id.
   * @return {@link Flux} of {@link OUT}.
   */
  public abstract Flux<OUT> findById(ID id);

  /**
   * Saves an instance of {@link OUT}.
   *
   * @param item Instance to save.
   * @return {@link Mono} of {@link OUT}.
   */
  public abstract Mono<OUT> save(OUT item);

  /**
   * Deletes a row by id.
   *
   * @param id Target id.
   * @return {@link Mono} of {@link Integer} (amount of rows deleted {0,})
   */
  public abstract Mono<Integer> deleteById(ID id);

  /**
   * Returns rows with an id LIKE {@link String}.
   *
   * @param idToMatch {@link String} to match.
   * @return {@link Flux} of {@link OUT}.
   */
  public abstract Flux<OUT> idLike(String idToMatch);
}
