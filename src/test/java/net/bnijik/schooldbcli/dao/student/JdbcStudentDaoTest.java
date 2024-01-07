package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;
import net.bnijik.schooldbcli.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ContextConfiguration(classes = {StudentQueries.class, JdbcStudentDaoConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/drop_create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcStudentDaoTest {

    @Autowired
    private JdbcStudentDao studentDao;

    @Test
    @DisplayName("when saving student should save student")
    void whenSavingStudentShouldSaveStudent() {

        Student expectedStudent = new Student(4, 1, "John", "Doe");

        assertEquals(expectedStudent.studentId(), studentDao.save(expectedStudent));
    }

    @Test
    @DisplayName("when finding all students should return stream of all students")
    void whenFindingAllStudentsShouldReturnStreamOfAllStudents() {

        Student expectedStudent1 = new Student(1L, 1L, "Jane", "Doe");
        Student expectedStudent2 = new Student(2L, 2L, "Student", "ToRemove");
        Student expectedStudent3 = new Student(3L, 2L, "Student2", "McStudent2");

        final Stream<Student> studentStream = studentDao.findAll(Page.of(1, 5));
        final long count = studentStream.filter(c -> c.equals(expectedStudent1) ||
                c.equals(expectedStudent2) ||
                c.equals(expectedStudent3)).count();
        assertEquals(3, count);
    }

    @Test
    @DisplayName("when finding student by id should return the correct student")
    void whenFindingStudentByIdShouldReturnTheCorrectStudent() {
        Student expectedStudent = new Student(1L, 1L, "Jane", "Doe");
        Optional<Student> retrievedStudentWrapper = studentDao.findById(expectedStudent.studentId());

        assertTrue(retrievedStudentWrapper.isPresent(), "Student is not present");
        assertEquals(expectedStudent, retrievedStudentWrapper.get());
    }

    @Test
    @DisplayName("when updating existing student should update student")
    void whenUpdatingExistingStudentShouldUpdateStudent() {
        assertTrue(studentDao.update(new Student(3L, 1, "Student2ModName", "McStudent2"), 3L));
    }

    @Test
    @DisplayName("when deleting a student should remove it from the database")
    void whenDeletingAStudentShouldRemoveItFromTheDatabase() {
        final Student studentToRemove = new Student(2, 2, "Student", "ToRemove");
        assertTrue(studentDao.delete(studentToRemove.studentId()));
    }

    @Test
    @DisplayName("when finding all students enrolled in given course should return correct students")
    void whenFindingAllStudentsEnrolledInGivenCourseShouldReturnCorrectStudents() {
        final Student student1 = new Student(1, 1, "Jane", "Doe");
        final Student student2 = new Student(2, 2, "Student", "ToRemove");
        final Course course2 = new Course(2, "Course2", "Description2");

        final List<Student> students = studentDao.findAllByCourseName(course2.courseName(), Page.of(1, 5)).toList();

        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }

    @Test
    @DisplayName("when enrolling a student in courses should update the student's enrollment")
    void whenEnrollingStudentInCoursesShouldUpdateEnrollment() {
        Student existingStudent = new Student(3, 2, "Student2", "McStudent2");

        String course1Name = "Course1";
        String course1Description = "Description1";
        String course2Name = "Course2";
        String course2Description = "Description2";
        Course course1 = new Course(1, course1Name, course1Description);
        Course course2 = new Course(2, course2Name, course2Description);

        assertTrue(studentDao.enrollInCourses(existingStudent.studentId(),
                                              Arrays.asList(course1.courseId(), course2.courseId())));

    }

    @Test
    @DisplayName("when withdrawing a student from a course should update the student's enrollment")
    void whenWithdrawingStudentFromCourseShouldUpdateEnrollment() {

        Student createdStudent = new Student(1, 1, "Jane", "Doe");
        Course courseToWithdraw = new Course(1, "Course1", "Description1");
        assertTrue(studentDao.withdrawFromCourse(createdStudent.studentId(), courseToWithdraw.courseId()));

    }
}