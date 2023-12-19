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

    public JdbcGroupDao(SimpleJdbcInsert insert, NamedParameterJdbcTemplate template, GroupQueries queries) {
        this.insert = insert.withTableName(GroupQueries.GROUP_TABLE_NAME)
                .usingGeneratedKeyColumns(GroupQueries.GROUP_ID_COLUMN);
        this.template = template;
        this.queries = queries;
    }

    @Override
    public Stream<Group> findAll(Page page) {
        String sql = queries.finaAll();
        try {
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
    public long save(Group group) {
        try {
            final Number id = insert.executeAndReturnKey(Map.of(GroupQueries.GROUP_NAME_COLUMN, group.groupName()));
            if (id.longValue() == 0) {
                log.error("Saving group failed. Returned ID: {}", id);
                return 0;
            }
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
            return template.queryForStream(sql, Map.of(GroupQueries.GROUP_ID_PARAM, id), rowMapper).findFirst();
        } catch (DataAccessException e) {
            log.error("Error finding group with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Group newGroup, long existingGroupId) {
        String sql = queries.update();
        try {
            final int rowsAffected = template.update(sql,
                                                     Map.of(GroupQueries.GROUP_NAME_PARAM,
                                                            newGroup.groupName(),
                                                            GroupQueries.GROUP_ID_PARAM,
                                                            existingGroupId));
            if (rowsAffected == 0) {
                log.error("Failed to update group with ID {}. No rows were affected.", existingGroupId);
                return false;
            }
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
            final int rowsAffected = template.update(sql, Map.of(GroupQueries.GROUP_ID_PARAM, id));
            if (rowsAffected == 0) {
                log.error("Failed to delete group with ID {}. No rows were affected.", id);
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting group with ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}
