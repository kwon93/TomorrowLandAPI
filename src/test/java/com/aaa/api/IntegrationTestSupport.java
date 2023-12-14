package com.aaa.api;


import com.aaa.api.domain.Users;
import com.aaa.api.repository.PostsRepository;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.PostsService;
import com.aaa.api.service.UsersService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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


    @AfterEach
    protected void tearDown() {
        usersRepository.deleteAllInBatch();
        postsRepository.deleteAllInBatch();
    }

    protected Users createUserInTest(){
        Users users = Users.builder()
                .email("kwon93@naver.com")
                .password("kdh1234")
                .name("kwon")
                .build();

        usersRepository.save(users);

        return users;
    }

}
