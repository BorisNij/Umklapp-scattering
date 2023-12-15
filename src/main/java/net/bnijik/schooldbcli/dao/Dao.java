package net.bnijik.schooldbcli.dao;

import java.util.Optional;
import java.util.stream.Stream;

public interface Dao<T> {
    Stream<T> findAll(Page page);

    void save(T t);

    Optional<T> findById(long id);

    void update(T t, long id);

    void delete(long id);
}
