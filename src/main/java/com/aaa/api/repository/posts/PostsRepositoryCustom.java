package com.aaa.api.repository.posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.repository.posts.dto.PostSearchForRepository;

import java.util.List;
import java.util.Optional;

public interface PostsRepositoryCustom {

    List<Posts> getList(PostSearchForRepository postSearch);

    Optional<Posts> getOneByPessimistLock(Long postsId);
}
