package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import com.aaa.api.service.dto.request.PostSearchForService;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import com.aaa.api.service.dto.response.PostsResponse;
import com.aaa.api.service.image.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final S3ImageUploader s3ImageUploader;

    @Transactional
    public PostsResponse create(final CreatePostsServiceRequest serviceRequest) {
        final Users user = usersRepository.findById(serviceRequest.getUserId())
                .orElseThrow(UserNotFound::new);

        final Posts posts = serviceRequest.toEntity(user);

        postsRepository.save(posts);
        return PostsResponse.of(posts);
    }

    public List<Posts> getPage(final PostSearchForService serviceDto) {
         return postsRepository.getList(serviceDto.toRepository());
    }

    public List<Posts> getAll(PostsCategory category){
        return postsRepository.getNoPagingLists(category);
    }

    @Transactional
    public PostsResponse getOne(final Long postsId) {
        final Posts posts = postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);

        posts.increaseViewCount();
        return PostsResponse.of(posts);
    }

    @Transactional
    public PostsResponse update(final UpdatePostsServiceRequest serviceRequest) {
        final Posts posts = findPostsById(serviceRequest.getPostsId());
        posts.update(serviceRequest.getTitle(), serviceRequest.getContent(), serviceRequest.getCategory());

        return PostsResponse.of(posts);
    }


    @Transactional
    public void delete(final Long id) {
        final Posts posts = findPostsById(id);
        if (StringUtils.hasText(posts.getImagePath())){
            s3ImageUploader.deleteImage(posts.getImagePath());
        }
        postsRepository.delete(posts);
    }


    private Posts findPostsById(final Long id) {
        return postsRepository.findById(id)
                .orElseThrow(PostNotfound::new);
    }

}
