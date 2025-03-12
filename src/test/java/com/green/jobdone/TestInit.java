package com.green.jobdone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test-init")
@Rollback(false)
@SpringBootTest
@Sql(scripts = {"classpath:test-import.sql"})
public class TestInit {
    @Test
    void init(){
    }
}
