package com.aaa.api.controller;

import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping("posts")
    public PostsResponse createPosts(@RequestBody CreatePostsRequest request){
        return postsService.createPosts(request);
    }


    @GetMapping("posts")
    public void getAllPosts(){

    }
}
