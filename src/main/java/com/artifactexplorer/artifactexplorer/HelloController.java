package com.artifactexplorer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @RequestMapping("/hello-world")
    String hello() {
        return "Hello World!";
    }
    // SQL sample
    @RequestMapping("calc")
    String calc() {
        return "";
    }
}
