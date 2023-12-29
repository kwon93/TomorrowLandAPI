package com.aaa.api;


import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.PostsRepository;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.PostsService;
import com.aaa.api.service.UsersService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
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

    //passwordEncoder
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @AfterEach
    protected void tearDown() {
        usersRepository.deleteAllInBatch();
        postsRepository.deleteAllInBatch();
    }

    @Transactional
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
