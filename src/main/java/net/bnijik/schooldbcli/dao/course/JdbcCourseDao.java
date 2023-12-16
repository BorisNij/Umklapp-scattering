package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;
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
public class JdbcCourseDao implements CourseDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcCourseDao.class);
    private final SimpleJdbcInsert insert;
    private final NamedParameterJdbcTemplate template;
    private final CourseQueries queries;
    private final RowMapper<Course> rowMapper = (rowSet, rowNum) -> new Course(rowSet.getLong("COURSE_ID"),
                                                                               rowSet.getString("COURSE_NAME"),
                                                                               rowSet.getString("COURSE_DESCRIPTION"));

    public JdbcCourseDao(NamedParameterJdbcTemplate template, SimpleJdbcInsert insert, CourseQueries queries) {
        this.insert = insert;
        this.template = template;
        this.queries = queries;
    }

    @Override
    public Stream<Course> findAll(Page page) {
        String sql = queries.finaAll();
        try {
            return template.queryForStream(sql,
                                           Map.of(Page.PAGE_LIMIT_PARAM,
                                                  page.getLimit(),
                                                  Page.PAGE_OFFSET_PARAM,
                                                  page.getOffset()),
                                           rowMapper);
        } catch (DataAccessException e) {
            log.error("Error retrieving all courses: {}", e.getMessage(), e);
            return Stream.empty();
        }
    }

    @Override
    public void save(Course course) {
        try {
            final Number id = insert.execute(Map.of(CourseQueries.COURSE_NAME_PARAM,
                                                    course.courseName(),
                                                    CourseQueries.COURSE_DESCRIPTION_PARAM,
                                                    course.courseDescription()));
            if (id.longValue() == 0) {
                log.error("Saving course failed. No rows were affected.");
            }
        } catch (DataAccessException e) {
            log.error("Error saving {} course: {}", course.courseName(), e.getMessage(), e);
        }
    }

    @Override
    public Optional<Course> findById(long id) {
        String sql = queries.findById();
        try {
            return template.queryForStream(sql, Map.of(CourseQueries.COURSE_ID_PARAM, id), rowMapper).findFirst();
        } catch (DataAccessException e) {
            log.error("Error finding course with ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Course newCourse, long existingCourseId) {
        String sql = queries.update();
        try {
            template.update(sql,
                            Map.of(CourseQueries.COURSE_NAME_PARAM,
                                   newCourse.courseName(),
                                   CourseQueries.COURSE_DESCRIPTION_PARAM,
                                   newCourse.courseDescription(),
                                   CourseQueries.COURSE_ID_PARAM,
                                   existingCourseId));
        } catch (DataAccessException e) {
            log.error("Error updating course with ID {}: {}", existingCourseId, e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = queries.deleteById();
        try {
            template.update(sql, Map.of(CourseQueries.COURSE_ID_PARAM, id));
        } catch (DataAccessException e) {
            log.error("Error deleting course with ID {}: {}", id, e.getMessage(), e);
        }
    }
}
