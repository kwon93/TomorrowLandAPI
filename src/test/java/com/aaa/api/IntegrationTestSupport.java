package com.aaa.api;


import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.like.PostsLikeRepository;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.service.*;
import com.aaa.api.service.image.ImageService;
import com.aaa.api.service.image.S3ImageUploader;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;

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
    @Autowired
    protected CommentRepository commentRepository;

    //passwordEncoder
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    //ImageService
    @Autowired
    protected ImageService imageService;
    @Autowired
    protected S3ImageUploader imageUploader;

    //PostsLikeService
    @Autowired
    protected PostsLikeService likeService;
    @Autowired
    protected PostsLikeRepository likeRepository;

    //JwtKey
    @Value("${jwt.secretKey}")
    protected String secretKey;
    protected SecretKey key;

    @BeforeEach
    protected void tearDown() {
        likeRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
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
    protected Users createUserInTest(Integer point, String email){
        Users users = Users.builder()
                .email(email)
                .password(passwordEncoder.encode("kdh1234"))
                .name("kwon")
                .point(point)
                .role(Role.ADMIN)
                .build();

        usersRepository.save(users);

        return users;
    }


    protected Posts createPostInTest(Users users) {
        Posts posts = Posts.builder()
                .user(users)
                .title("제목")
                .content("내용")
                .postsCategory(PostsCategory.LIFE)
                .build();

        return postsRepository.save(posts);
    }

}
