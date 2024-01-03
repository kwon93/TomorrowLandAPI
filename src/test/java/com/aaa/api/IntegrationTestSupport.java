package com.aaa.api;


import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.Posts.PostsRepository;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.CommentService;
import com.aaa.api.service.PostsService;
import com.aaa.api.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    //Posts
    @Autowired
    protected PostsService postsService;
    @Autowired
    protected PostsRepository postsRepository;


    //Users
    @Autowired
    protected UsersService usersService;
    @Autowired
    protected UsersRepository usersRepository;

    //Auth
    @Autowired
    protected AuthService authService;

    //Comment
    @Autowired
    protected CommentService commentService;

    //passwordEncoder
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    protected void tearDown() {
        postsRepository.deleteAllInBatch();
        usersRepository.deleteAllInBatch();
    }

    protected Users createUserInTest(){
        Users users = Users.builder()
                .email("kwon93@naver.com")
                .password(passwordEncoder.encode("kdh1234"))
                .name("kwon")
                .role(Role.ADMIN)
                .build();

        usersRepository.save(users);

        return users;
    }

}
