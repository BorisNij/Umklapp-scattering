package net.bnijik.schooldbcli.dao.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
public class JdbcGroupDaoTestConfig {
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private GroupQueries queries;

    @Bean
    public JdbcGroupDao jdbcGroupDao() {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template.getJdbcTemplate());
        return new JdbcGroupDao(template, insert, queries);
    }
}
