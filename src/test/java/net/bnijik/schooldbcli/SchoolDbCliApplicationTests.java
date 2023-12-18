package net.bnijik.schooldbcli;

import net.bnijik.schooldbcli.config.DataAccessConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = DataAccessConfig.class)
@SpringBootTest
class SchoolDbCliApplicationTests {

    @Test
    void contextLoads() {
    }

}
