package com.aaa.api;


import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.SseRepository;
import com.aaa.api.service.SseService;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.like.PostsLikeRepository;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.service.*;
import com.aaa.api.service.image.ImageFileNameProcessor;
import com.aaa.api.service.image.LocalImageStorageManager;
import com.aaa.api.service.image.S3ImageStorageManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class IntegrationTestSupport {

    @Autowired
    protected ObjectMapper objectMapper;
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
    @Autowired
    protected CommentNotificationService commentNotificationService;

    //passwordEncoder
    @Autowired
    protected PasswordEncoder passwordEncoder;

    //ImageService
    @Autowired
    protected ImageFileNameProcessor imageFileNameProcessor;
    @Autowired
    protected S3ImageStorageManager s3ImageStorageManager;
    @Autowired
    protected LocalImageStorageManager localImageStorageManager;

    //PostsLikeService
    @Autowired
    protected PostsLikeService likeService;
    @Autowired
    protected PostsLikeRepository likeRepository;

    //SseEmitter
    @Autowired
    protected SseService sseService;
    @Autowired
    protected SseRepository sseRepository;

    //Redis
    @Autowired
    protected RedisTemplate redisTemplate;




    @BeforeEach
    protected void tearDown() {
        likeRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        postsRepository.deleteAllInBatch();
        usersRepository.deleteAllInBatch();
    }

    protected Users createUserInTest(){
        Users users = Users.builder()
                .id(1L)
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
                .title("foo title")
                .content("bar content")
                .postsCategory(PostsCategory.LIFE)
                .build();

        postsRepository.save(posts);
        return posts;
    }

}
