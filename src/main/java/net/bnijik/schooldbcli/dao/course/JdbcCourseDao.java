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
    private final RowMapper<Course> rowMapper = (rowSet, rowNum) -> new Course(rowSet.getLong(CourseQueries.COURSE_ID_COLUMN),
                                                                               rowSet.getString(CourseQueries.COURSE_NAME_COLUMN),
                                                                               rowSet.getString(CourseQueries.COURSE_DESCRIPTION_COLUMN));

    public JdbcCourseDao(NamedParameterJdbcTemplate template, SimpleJdbcInsert insert, CourseQueries queries) {
        this.insert = insert.withTableName(CourseQueries.COURSE_TABLE_NAME)
                .usingGeneratedKeyColumns(CourseQueries.COURSE_ID_COLUMN);
        this.template = template;
        this.queries = queries;
    }


    @Override
    public long save(Course course) {
        try {
            final Number id = insert.executeAndReturnKey(Map.of(CourseQueries.COURSE_NAME_COLUMN,
                                                                                course.courseName(),
                                                                                CourseQueries.COURSE_DESCRIPTION_COLUMN,
                                                                                course.courseDescription()));
            if (id.longValue() == 0) {
                log.error("Saving course failed. Returned ID: {}", id);
                return 0;
            }
            return id.longValue();
        } catch (DataAccessException e) {
            log.error("Error saving {} course: {}", course.courseName(), e.getMessage(), e);
            throw e;
        }
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
            throw e;
        }
    }

    @Override
    public Stream<Course> findAllForStudent(int studentId, Page page) {
        String sql = queries.findAllForStudent();
        try {

            return template.queryForStream(sql,
                                           Map.of(CourseQueries.STUDENT_ID_PARAM,
                                                  studentId,
                                                  Page.PAGE_LIMIT_PARAM,
                                                  page.getLimit(),
                                                  Page.PAGE_OFFSET_PARAM,
                                                  page.getOffset()),
                                           rowMapper);
        } catch (DataAccessException e) {
            log.error("Error retrieving all courses having for student having ID {}: {}", studentId, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public Optional<Course> findById(long id) {
        String sql = queries.findById();
        try {
            return template.queryForStream(sql, Map.of(CourseQueries.COURSE_ID_PARAM, id), rowMapper).findFirst();
        } catch (DataAccessException e) {
            log.error("Error finding course with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean update(Course newCourse, long existingCourseId) {
        String sql = queries.update();
        try {
            final int rowsAffected = template.update(sql,
                                                     Map.of(CourseQueries.COURSE_NAME_PARAM,
                                                            newCourse.courseName(),
                                                            CourseQueries.COURSE_DESCRIPTION_PARAM,
                                                            newCourse.courseDescription(),
                                                            CourseQueries.COURSE_ID_PARAM,
                                                            existingCourseId));
            if (rowsAffected == 0) {
                log.error("Failed to update course with ID {}. No rows were affected.", existingCourseId);
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error("Error updating course with ID {}: {}", existingCourseId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = queries.deleteById();
        try {
            final int rowsAffected = template.update(sql, Map.of(CourseQueries.COURSE_ID_PARAM, id));
            if (rowsAffected == 0) {
                log.error("Failed to delete course with ID {}. No rows were affected.", id);
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error("Error deleting course with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
