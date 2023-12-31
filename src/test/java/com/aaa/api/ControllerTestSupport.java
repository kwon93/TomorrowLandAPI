package com.aaa.api;

import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.config.security.jwt.JwtTokenReIssueProvider;
import com.aaa.api.controller.AuthController;
import com.aaa.api.controller.PostsController;
import com.aaa.api.controller.UsersController;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.PostsService;
import com.aaa.api.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest( controllers = {PostsController.class, UsersController.class, AuthController.class})
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
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
    protected JwtTokenReIssueProvider reIssueProvider;


}
