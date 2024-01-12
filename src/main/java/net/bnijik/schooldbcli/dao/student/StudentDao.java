package net.bnijik.schooldbcli.dao.student;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Student;

import java.util.List;
import java.util.stream.Stream;

public interface StudentDao extends Dao<Student> {
    Stream<Student> findAllByCourseName(String courseName, Page page);

    boolean enrollInCourses(long studentId, List<Long> courseIds);

    boolean withdrawFromCourse(long studentId, long courseId);

}
