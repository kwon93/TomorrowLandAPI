package com.aaa.api.docs;

import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.CommentNotificationService;
import com.aaa.api.service.SseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.tomorrow.com", uriPort = 443)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public abstract class RestDocsIntegrationSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UsersRepository usersRepository;
    protected BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    protected CommentNotificationService commentNotificationService;
    @Autowired
    protected SseService sseService;

    @BeforeEach
    void setUp() {
        Users user = Users.builder()
                .email("async@naver.com")
                .password(passwordEncoder.encode("kdh1234"))
                .name("foo")
                .role(Role.USER)
                .userLevel(UserLevel.Beginner)
                .build();
        usersRepository.save(user);
    }
}
