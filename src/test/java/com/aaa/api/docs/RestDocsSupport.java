package com.aaa.api.docs;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.config.security.jwt.JwtTokenReIssueProvider;
import com.aaa.api.controller.*;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.service.*;
import com.aaa.api.service.image.ImageService;
import com.aaa.api.service.image.S3ImageUploader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.tomorrow.com", uriPort = 443)
@ActiveProfiles("test")
@WebMvcTest( controllers = {
        PostsController.class,
        UsersController.class,
        AuthController.class,
        CommentController.class,
        ImageController.class,
        PostsLikeController.class
} )
public abstract class RestDocsSupport {

    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected CustomUserPrincipal userPrincipal;
    @MockBean
    protected PostsService postsService;
    @MockBean
    protected UsersService usersService;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected CommentService commentService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected JwtTokenReIssueProvider reIssueProvider;
    @MockBean
    protected ImageService imageService;
    @MockBean
    protected S3ImageUploader imageUploader;
    @MockBean
    protected PostsLikeService likeService;
    @MockBean
    protected SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        Users user = Users.builder()
                .id(1L)
                .role(Role.ADMIN)
                .email("test@test.net")
                .password("password")
                .build();

        userPrincipal = CustomUserPrincipal.of(user);
    }
}

