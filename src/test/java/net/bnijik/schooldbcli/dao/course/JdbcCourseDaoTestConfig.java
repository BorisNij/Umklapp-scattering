package net.bnijik.schooldbcli.dao.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class JdbcCourseDaoTestConfig {
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private CourseQueries queries;

    @Bean
    public JdbcCourseDao jdbcCourseDao() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template.getJdbcTemplate());
        return new JdbcCourseDao(template, insert, queries);
    }
}
