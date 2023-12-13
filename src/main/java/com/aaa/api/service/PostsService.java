package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.PostSearch;
import com.aaa.api.dto.request.UpdatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public PostsResponse createPosts(CreatePostsRequest request) {
        Posts posts = Posts.of(request);
        postsRepository.save(posts);

        return PostsResponse.of(posts);
    }

    public List<PostsResponse> getAll(PostSearch postSearch) {

        return postsRepository.getList(postSearch).stream()
                .map(PostsResponse::of)
                .toList();
    }

    public PostsResponse getOne(Long id) {
        Posts posts = findPostsById(id);

        return PostsResponse.of(posts);
    }

    @Transactional
    public PostsResponse update(UpdatePostsRequest request, Long id) {
        Posts posts = findPostsById(id);

        Posts updatedPosts = posts.updatePosts(request);
        postsRepository.save(updatedPosts);

        return PostsResponse.of(updatedPosts);
    }


    @Transactional
    public void delete(Long id) {
        Posts posts = findPostsById(id);
        postsRepository.delete(posts);
    }


    private Posts findPostsById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(PostNotfound::new);
        return posts;
    }

}
