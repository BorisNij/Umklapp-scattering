package net.bnijik.schooldbcli.dao;

import java.util.Optional;
import java.util.stream.Stream;

public interface Dao<T> {
    Stream<T> findAll(Page page);

    long save(T t);

    Optional<T> findById(long id);

    boolean update(T t, long id);

    boolean delete(long id);
}
