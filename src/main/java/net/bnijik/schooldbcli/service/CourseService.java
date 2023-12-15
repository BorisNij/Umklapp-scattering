package net.bnijik.schooldbcli.service;

import net.bnijik.schooldbcli.dao.CourseDao;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
}
