package net.bnijik.schooldbcli.dao;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public class CourseQueries {
    @Value("${course.create-course}")
    private String createCourse;
    @Value("${course.find-by-id}")
    private String findById;
    @Value("${course.find-by-name}")
    private String findByName;
    @Value("${course.find-all}")
    private String finaAll;
    @Value("${course.delete-by-id}")
    private String deleteById;

    public String createCourse() {
        return createCourse;
    }

    public String findById() {
        return findById;
    }

    public String findByName() {
        return findByName;
    }

    public String finaAll() {
        return finaAll;
    }

    public String deleteById() {
        return deleteById;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CourseQueries) obj;
        return Objects.equals(this.createCourse, that.createCourse) &&
                Objects.equals(this.findById, that.findById) &&
                Objects.equals(this.findByName, that.findByName) &&
                Objects.equals(this.finaAll, that.finaAll) &&
                Objects.equals(this.deleteById, that.deleteById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createCourse, findById, findByName, finaAll, deleteById);
    }

    @Override
    public String toString() {
        return "CourseQueries[" +
                "createCourse=" + createCourse + ", " +
                "findById=" + findById + ", " +
                "findByName=" + findByName + ", " +
                "finaAll=" + finaAll + ", " +
                "deleteById=" + deleteById + ']';
    }

}
