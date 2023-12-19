package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ContextConfiguration(classes = CourseQueries.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcCourseDaoTest {
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private CourseQueries queries;
    private JdbcCourseDao courseDao;

    @BeforeEach
    void setUp() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template.getJdbcTemplate());
        courseDao = new JdbcCourseDao(template, insert, queries);
    }

    @Test
    @DisplayName("when finding course by id should return the right course")
    void whenFindingCourseByIdShouldReturnTheRightCourse() {
        final Course expected = new Course(2L, "Course2", "Description2");
        final Optional<Course> optionalCourse = courseDao.findById(expected.courseId());

        assertTrue(optionalCourse.isPresent(), "Course is not present");
        assertEquals(expected, optionalCourse.get());
    }


    @Test
    @DisplayName("when saving course should save course")
    void whenSavingCourseOfCertainNameShouldSaveCourse() {
        Course fourthCourse = new Course(4L, "Course3", "Course3 description");
        assertEquals(fourthCourse.courseId(), courseDao.save(fourthCourse));
    }

    @Test
    @DisplayName("when getting all courses should return a stream of all courses")
    void whenGettingAllCoursesShouldReturnStreamOfAllCourses() {
        Course expectedCourse1 = new Course(1L, "Course1", "Description1");
        Course expectedCourse2 = new Course(2L, "Course2", "Description2");
        Course expectedCourse3 = new Course(3L, "Course to delete", "Description");

        final Stream<Course> courseStream = courseDao.findAll(Page.of(1, 5));

        final long count = courseStream.filter(c -> c.equals(expectedCourse1) ||
                c.equals(expectedCourse2) ||
                c.equals(expectedCourse3)).count();
        assertEquals(3, count);
    }

    @Test
    @DisplayName("when deleting course by id should remove it from the database")
    void whenDeletingCourseByIdShouldRemoveFromDatabase() {
        assertTrue(courseDao.delete(1L));
    }

    @Test
    @DisplayName("when updating existing course should update course")
    void whenUpdatingExistingCourseShouldUpdateCourseWithRightDetails() {
        assertTrue(courseDao.update(new Course(3L, "Modified course name", "Modified description"), 3L));
    }
}