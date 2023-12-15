package net.bnijik.schooldbcli.dao;

import net.bnijik.schooldbcli.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCourseDao implements CourseDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcCourseDao.class);
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CourseQueries courseQueries;
    private final RowMapper<Course> courseRowMapper = (rowSet, rowNum) -> new Course(rowSet.getLong("COURSE_ID"),
                                                                                     rowSet.getString("COURSE_NAME"),
                                                                                     rowSet.getString(
                                                                                             "COURSE_DESCRIPTION"));

    public JdbcCourseDao(NamedParameterJdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert, CourseQueries queries) {
        this.jdbcInsert = jdbcInsert;
        this.jdbcTemplate = jdbcTemplate;
        this.courseQueries = queries;
    }

    @Override
    public List<Course> findAll() {
        return null;
    }

    @Override
    public void save(Course course) {
        final Number id = jdbcInsert.executeAndReturnKey(course.toMap());
        findById(id.longValue()).orElseThrow(() -> new IllegalStateException(""));
    }

    @Override
    public Optional<Course> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void update(Course course, long id) {

    }

    @Override
    public void delete(long id) {

    }
}
