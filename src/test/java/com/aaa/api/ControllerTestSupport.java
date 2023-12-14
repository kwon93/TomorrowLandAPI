package com.aaa.api;

import com.aaa.api.controller.PostsController;
import com.aaa.api.repository.PostsRepository;
import com.aaa.api.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostsController.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected PostsService postsService;


}
