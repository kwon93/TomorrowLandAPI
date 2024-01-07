package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.Posts.PostsRepository;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import com.aaa.api.service.dto.request.PostSearchForService;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import com.aaa.api.service.dto.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public PostsResponse create(final CreatePostsServiceRequest serviceRequest) {
        final Users user = usersRepository.findById(serviceRequest.getUserId())
                .orElseThrow(UserNotFound::new);

        final Posts posts = serviceRequest.toEntity(user);

        postsRepository.save(posts);
        return PostsResponse.of(posts);
    }

    public List<Posts> getAll(final PostSearchForService serviceDto) {
         return postsRepository.getList(serviceDto.toRepository());
    }

    @Transactional
    public PostsResponse getOne(final Long postsId) {
        Posts posts = postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);
        posts.increaseViewCount();

        return PostsResponse.of(posts);
    }

    @Transactional
    public PostsResponse update(final UpdatePostsServiceRequest serviceRequest) {
        Posts posts = findPostsById(serviceRequest.getPostsId());

        Posts updatedPosts =
                postsRepository.save(serviceRequest.updatePosts(posts));

        return PostsResponse.of(updatedPosts);
    }


    @Transactional
    public void delete(final Long id) {
        Posts posts = findPostsById(id);
        postsRepository.delete(posts);
    }


    private Posts findPostsById(final Long id) {
        return postsRepository.findById(id)
                .orElseThrow(PostNotfound::new);
    }

}
