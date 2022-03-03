package project.society.data.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import project.society.data.dto.DTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class GenericReactiveDAO {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public GenericReactiveDAO(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    public Mono<Boolean> existsById(String id, Class<? extends DTO<?>> clazz) {
        String idColumnName = this.getIdColumnName(clazz);
        return this.r2dbcEntityTemplate.exists(Query.query(Criteria.where(idColumnName).is(id)), clazz);
    }

    public <T extends DTO<?>> Mono<T> findOneById(String id, Class<T> clazz) {
        String idColumnName = this.getIdColumnName(clazz);
        return this.r2dbcEntityTemplate.selectOne(Query.query(Criteria.where(idColumnName).is(id)), clazz);
    }

    public <T extends DTO<?>> Flux<T> findById(String id, Class<T> clazz) {
        String idColumnName = this.getIdColumnName(clazz);
        return this.r2dbcEntityTemplate.select(Query.query(Criteria.where(idColumnName).is(id)), clazz);
    }

    public Mono<Integer> deleteById(String id, Class<? extends DTO<?>> clazz) {
        String idColumnName = this.getIdColumnName(clazz);
        return this.r2dbcEntityTemplate.delete(Query.query(Criteria.where(idColumnName).is(id)), clazz);
    }

    /**
     * Saves an item. Inserts if item does not already exist. Updates if it does.
     * @param item Item to save.
     * @param clazz {@link Class} of item to save.
     * @param <T> Type of item to save.
     * @return {@link Mono} of {@link T}.
     */
    public <T extends DTO<?>> Mono<T> save(T item, Class<T> clazz) {
        return this.existsById(item.getId().toString(), clazz)
                .flatMap(exists -> (exists)
                        ? this.r2dbcEntityTemplate.insert(item)
                        : this.r2dbcEntityTemplate.update(item));
    }

    public <T extends DTO<?>> Flux<T> idLike(String idToMatch, Class<T> clazz) {
        String idColumnName = this.getIdColumnName(clazz);
        return this.r2dbcEntityTemplate.select(Query.query(Criteria.where(idColumnName).like(idToMatch)), clazz);
    }

    /**
     * Goes through the classes variable annotations, if a variable has the {@link Id} annotation.
     * We check if it also has a {@link Column} annotation, we then return the value from the {@link Column}
     * annotation. If it does not have a {@link Column} annotation, we return the field name instead.
     * @param clazz {@link Class} we wan to extract id column name from.
     * @return {@link String} id column name.
     */
    private String getIdColumnName(Class<? extends DTO<?>> clazz) {
        for(Field field : clazz.getDeclaredFields()) {
            for(Annotation annotation : field.getDeclaredAnnotations()) {
                if(annotation instanceof Id) {
                    Column column = field.getDeclaredAnnotation(Column.class);
                    if(column != null) {
                        return column.value();
                    }
                    return field.getName();
                }
            }
        }
        throw new NullPointerException("No ID column.");
    }
}
