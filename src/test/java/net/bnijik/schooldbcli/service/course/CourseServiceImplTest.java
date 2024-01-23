package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dao.course.CourseDao;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    CourseDao courseDao;

    @Mock
    CourseMapper courseMapper;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {

        Course course = getCourseEntity();

        when(courseDao.save(any(Course.class))).thenReturn(course.courseId());

        CourseDto newCourseDto = new CourseDto(123L, "Math", "Math Description");
        when(courseMapper.dtoToModel(any(CourseDto.class))).thenReturn(course);
        final long id = courseService.save(newCourseDto);

        assertNotEquals(0, id);
    }

    private Course getCourseEntity() {
        return new Course(23L, "Some cool course", "Coolest description");
    }
}