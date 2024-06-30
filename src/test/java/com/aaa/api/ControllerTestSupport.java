package com.aaa.api;

import com.aaa.api.config.CustomMockSecurityContext;
import com.aaa.api.controller.*;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.*;
import com.aaa.api.service.image.ImageService;
import com.aaa.api.service.image.S3ImageUploader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest( controllers = {
        PostsController.class,
        UsersController.class,
        AuthController.class,
        CommentController.class,
        ImageController.class,
        PostsLikeController.class
} )
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected PostsService postsService;
    @MockBean
    protected UsersService usersService;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected CommentService commentService;
    @MockBean
    protected ImageService imageService;
    @MockBean
    protected S3ImageUploader imageUploader;
    @MockBean
    protected PostsLikeService likeService;
    @MockBean
    protected SimpMessagingTemplate simpMessagingTemplate;


    //통합테스트환경에서 뺄지 고민중..
    @MockBean
    protected UsersRepository usersRepository;
    @InjectMocks
    protected CustomMockSecurityContext customMockSecurityContext;


}
