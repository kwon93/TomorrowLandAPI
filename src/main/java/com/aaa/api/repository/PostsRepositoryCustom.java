package com.aaa.api.repository;

import com.aaa.api.domain.Posts;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepositoryCustom {

    List<Posts> getList(int page);
}
