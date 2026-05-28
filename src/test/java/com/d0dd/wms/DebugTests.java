package com.d0dd.wms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class DebugTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSchema() {
        System.out.println("Checking outbound table columns:");
        List<Map<String, Object>> columns = jdbcTemplate.queryForList("SHOW COLUMNS FROM outbound");
        for (Map<String, Object> col : columns) {
            System.out.println(col);
        }

        System.out.println("Checking inbound table columns:");
        columns = jdbcTemplate.queryForList("SHOW COLUMNS FROM inbound");
        for (Map<String, Object> col : columns) {
            System.out.println(col);
        }
        
        System.out.println("Checking sys_role table columns:");
        columns = jdbcTemplate.queryForList("SHOW COLUMNS FROM sys_role");
        for (Map<String, Object> col : columns) {
            System.out.println(col);
        }
    }
}
