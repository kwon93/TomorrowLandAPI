package com.aaa.api.repository.Posts;

import com.aaa.api.domain.Posts;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long>, PostsRepositoryCustom {

}
