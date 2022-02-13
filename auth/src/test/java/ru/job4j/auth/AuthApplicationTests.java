package ru.job4j.auth;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AuthApplicationTests {

    @Autowired
    WebApplicationContext context;

    @Test
    void contextLoads() {
        Assert.assertNotNull(context);
    }

}
