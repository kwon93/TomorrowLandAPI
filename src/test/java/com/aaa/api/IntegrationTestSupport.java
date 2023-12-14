package com.aaa.api;


import com.aaa.api.repository.PostsRepository;
import com.aaa.api.service.PostsService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected PostsService postsService;
    @Autowired
    protected PostsRepository postsRepository;
    @AfterEach
    protected void tearDown() {
        postsRepository.deleteAllInBatch();
    }
}
