package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dao.course.CourseDao;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CourseServiceImpl.class, CourseServiceImplTestConfig.class})
class CourseServiceImplTest {

    @MockBean
    CourseDao courseDao;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {

        Course course = getCourseEntity();

        when(courseDao.findById(course.courseId())).thenReturn(Optional.empty());
        when(courseDao.save(any(Course.class))).thenReturn(course.courseId());

        CourseDto newCourseDto = new CourseDto(123L, "Math", "Math Description");
        final long id = courseService.save(newCourseDto);

        assertNotEquals(0, id);
//        assertEquals(newCourseDto.getName(), courseDto.getName());
//        assertEquals(newCourseDto.getDescription(), courseDto.getDescription());

        verify(courseDao).save(any(Course.class));
    }

    private Course getCourseEntity() {
        return new Course(23L, "Some cool course", "Coolest description");
    }
}