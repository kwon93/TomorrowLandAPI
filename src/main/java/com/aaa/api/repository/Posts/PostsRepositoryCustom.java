package com.aaa.api.repository.Posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.repository.Posts.dto.PostSearchForRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface PostsRepositoryCustom {

    List<Posts> getList(PostSearchForRepository postSearch);

    Optional<Posts> getOneByPessimistLock(Long postsId);
}
