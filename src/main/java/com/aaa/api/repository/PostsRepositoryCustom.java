package com.aaa.api.repository;

import com.aaa.api.domain.Posts;
import com.aaa.api.dto.request.PostSearch;

import java.util.List;

public interface PostsRepositoryCustom {

    List<Posts> getList(PostSearch postSearch);
}
