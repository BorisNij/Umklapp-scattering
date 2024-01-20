package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.dao.course.CourseDao;
import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.mapper.CourseMapper;
import net.bnijik.schooldbcli.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseDao courseDao, CourseMapper courseMapper) {
        this.courseDao = courseDao;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<CourseDto> findAll(Page page) {
        final List<Course> courses = courseDao.findAll(page);
        return courseMapper.modelsToDtos(courses);
    }

    @Override
    public long save(CourseDto courseDto) {
        final Course course = courseMapper.dtoToModel(courseDto);
        return courseDao.save(course);
    }

    @Override
    public Optional<CourseDto> findById(long id) {
        final Optional<Course> courseOptional = courseDao.findById(id);
        return courseOptional.map(courseMapper::modelToDto);
    }

    @Override
    public boolean update(CourseDto courseDto, long id) {
        final Course course = courseMapper.dtoToModel(courseDto);
        return courseDao.update(course, id);
    }

    @Override
    public boolean delete(long id) {
        return courseDao.delete(id);
    }

    @Override
    public List<CourseDto> findAllForStudent(int studentId, Page page) {
        final List<Course> courses = courseDao.findAllForStudent(studentId, page);
        return courseMapper.modelsToDtos(courses);
    }

}
