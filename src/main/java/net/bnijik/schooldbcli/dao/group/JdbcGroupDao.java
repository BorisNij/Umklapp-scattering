package net.bnijik.schooldbcli.dao.group;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class JdbcGroupDao implements GroupDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcGroupDao.class);
    private final SimpleJdbcInsert insert;
    private final NamedParameterJdbcTemplate template;
    private final GroupQueries queries;
    private final RowMapper<Group> rowMapper = (rowSet, rowNum) -> new Group(rowSet.getLong(GroupQueries.GROUP_ID_COLUMN),
                                                                             rowSet.getString(GroupQueries.GROUP_NAME_COLUMN));

    public JdbcGroupDao(NamedParameterJdbcTemplate template, SimpleJdbcInsert insert, GroupQueries queries) {
        this.insert = insert.withTableName(GroupQueries.GROUP_TABLE_NAME)
                .usingGeneratedKeyColumns(GroupQueries.GROUP_ID_COLUMN);
        this.template = template;
        this.queries = queries;
    }

    @Override
    public long save(Group group) {
        try {
            log.debug("Saving group {}", group.groupName());
            final Number id = insert.executeAndReturnKey(Map.of(GroupQueries.GROUP_NAME_COLUMN, group.groupName()));
            if (id.longValue() == 0) {
                log.error("Saving group failed. Returned ID: {}", id);
                return 0;
            }
            log.info("Successfully saved new group with ID {}", id);
            return id.longValue();
        } catch (DataAccessException e) {
            log.error("Error saving {} group: {}", group.groupName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Group> findById(long id) {
        String sql = queries.findById();
        try {
            log.debug("Finding group with ID {}", id);
            return template.queryForStream(sql, Map.of(GroupQueries.GROUP_ID_PARAM, id), rowMapper).findFirst();
        } catch (DataAccessException e) {
            log.error("Error finding group with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Stream<Group> findAll(Page page) {
        String sql = queries.finaAll();
        try {
            log.debug("Finding all groups with limit {}, offset {}", page.getLimit(), page.getOffset());
            return template.queryForStream(sql,
                                           Map.of(Page.PAGE_LIMIT_PARAM,
                                                  page.getLimit(),
                                                  Page.PAGE_OFFSET_PARAM,
                                                  page.getOffset()),
                                           rowMapper);
        } catch (DataAccessException e) {
            log.error("Error retrieving all groups: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Group> findByName(String name) {
        String sql = queries.findByName();
        try {
            log.debug("Finding group by name {}", name);
            return template.queryForStream(sql, Map.of(GroupQueries.GROUP_NAME_PARAM, name), rowMapper).findFirst();
        } catch (DataAccessException e) {
            log.error("Error finding group with name {}: {}", name, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Stream<Group> findAllByMaxStudentCount(int maxStudentCount, Page page) {
        String sql = queries.findAllByMaxStudCount();
        try {
            log.debug("Finding all groups having at most {} students with limit {} and offset {}",
                      maxStudentCount,
                      page.getLimit(),
                      page.getOffset());
            return template.queryForStream(sql,
                                           Map.of(GroupQueries.GROUP_STUD_COUNT_PARAM,
                                                  maxStudentCount,
                                                  Page.PAGE_LIMIT_PARAM,
                                                  page.getLimit(),
                                                  Page.PAGE_OFFSET_PARAM,
                                                  page.getOffset()),
                                           rowMapper);
        } catch (DataAccessException e) {
            log.error("Error retrieving all groups having at most {} students: {}", maxStudentCount, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Group newGroup, long existingGroupId) {
        String sql = queries.update();
        try {
            log.debug("Updating group having ID {} with new name {}", existingGroupId, newGroup.groupName());
            final int rowsAffected = template.update(sql,
                                                     Map.of(GroupQueries.GROUP_NAME_PARAM,
                                                            newGroup.groupName(),
                                                            GroupQueries.GROUP_ID_PARAM,
                                                            existingGroupId));
            if (rowsAffected == 0) {
                log.warn("No rows were affected while attempting to update group ID {}", existingGroupId);
                return false;
            }
            log.info("Successfully updated group having ID {} with new name {}", existingGroupId, newGroup.groupName());
            return true;
        } catch (DataAccessException e) {
            log.error("Error updating group with ID {}: {}", existingGroupId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = queries.deleteById();
        try {
            log.debug("Deleting group with ID {}", id);
            final int rowsAffected = template.update(sql, Map.of(GroupQueries.GROUP_ID_PARAM, id));
            if (rowsAffected == 0) {
                log.warn("No rows were affected while deleting group with {}", id);
                return false;
            }
            log.info("Successfully delete group with ID {}", id);
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting group with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
