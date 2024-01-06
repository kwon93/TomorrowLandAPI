package com.aaa.api.repository.Posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.repository.Posts.dto.PostSearchForRepository;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> getList(PostSearchForRepository postSearch);
}
