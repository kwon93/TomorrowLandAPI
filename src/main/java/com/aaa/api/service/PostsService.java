package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    public PostsResponse createPosts(CreatePostsRequest request) {

        Posts posts = Posts.of(request);

        postsRepository.save(posts);

        return PostsResponse.of(posts);
    }
}
