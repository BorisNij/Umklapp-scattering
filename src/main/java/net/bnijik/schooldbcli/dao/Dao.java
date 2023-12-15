package net.bnijik.schooldbcli.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();

    void save(T t);

    Optional<T> findById(long id);

    void update(T t, long id);

    void delete(long id);
}
