package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Group;

import java.util.Optional;
import java.util.stream.Stream;

public interface GroupDao extends Dao<Group> {
    Optional<Group> findByName(String groupName);

    Stream<Group> findAllByMaxStudentCount(int maxStudentCount, Page page);
}
