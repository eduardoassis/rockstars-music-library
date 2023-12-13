package com.rockstars.musiclibrary;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    @BeforeAll
    void beforeAll() {
        initDataBase();
    }

    @AfterAll
    void afterAll() {

    }

    public abstract void initDataBase();
}