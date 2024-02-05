package com.aaa.api.config;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.posts.PostsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DummyInitializer {

    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final PasswordEncoder passwordEncoder;
    private Users dummyUser;

    @PostConstruct
    public void userInit(){
         dummyUser = Users.builder()
                .id(1)
                .email("dummy@naver.com")
                .password(passwordEncoder.encode("abc123"))
                .name("kwon")
                .role(Role.USER)
                .point(200)
                .build();
        usersRepository.save(dummyUser);
    }

    @PostConstruct
    public void postInit(){
        List<Posts> posts = LongStream.range(0, 30)
                .mapToObj(i ->
                Posts.builder()
                        .user(dummyUser)
                        .title("DummyPosts " + i)
                        .content("DummyContent " + i)
                        .postsCategory(PostsCategory.DEV)
                        .build()
        ).collect(Collectors.toList());

        postsRepository.saveAll(posts);
    }

}
