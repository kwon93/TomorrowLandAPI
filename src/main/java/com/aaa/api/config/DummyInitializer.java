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
                .email("dummy@test.com")
                .password(passwordEncoder.encode("abc123"))
                .name("dummy")
                .role(Role.USER)
                .point(200)
                .build();
        usersRepository.save(dummyUser);
    }

    @PostConstruct
    public void postInit(){
        List<Posts> devPosts = LongStream.range(0, 10)
                .mapToObj(i ->
                Posts.builder()
                        .user(dummyUser)
                        .title("devPosts " + i)
                        .content("DummyContent " + i)
                        .postsCategory(PostsCategory.DEV)
                        .build()
        ).collect(Collectors.toList());

        postsRepository.saveAll(devPosts);

        List<Posts> mediaPosts = LongStream.range(0, 10)
                .mapToObj(i ->
                        Posts.builder()
                                .user(dummyUser)
                                .title("mediaPosts " + i)
                                .content("DummyContent " + i)
                                .postsCategory(PostsCategory.MEDIA)
                                .build()
                ).collect(Collectors.toList());

        postsRepository.saveAll(mediaPosts);

        List<Posts> etcPosts = LongStream.range(0, 23)
                .mapToObj(i ->
                        Posts.builder()
                                .user(dummyUser)
                                .title("etcPosts " + i)
                                .content("DummyContent " + i)
                                .postsCategory(PostsCategory.ETC)
                                .build()
                ).collect(Collectors.toList());

        postsRepository.saveAll(etcPosts);
    }

}
