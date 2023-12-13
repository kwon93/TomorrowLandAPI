package com.aaa.api.controller;

import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.PostSearch;
import com.aaa.api.dto.request.UpdatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping("posts")
    public PostsResponse createPosts(@RequestBody @Validated CreatePostsRequest request){
        return postsService.createPosts(request);
    }

    @GetMapping("posts")
    public List<PostsResponse> getAllPosts(PostSearch postSearch){
            return postsService.getAll(postSearch);
    }

    @GetMapping("posts/{postId}")
    public PostsResponse getOnePosts(@PathVariable("postId") Long id){
        return postsService.getOne(id);
    }

    @PatchMapping("posts/{postId}")
    public PostsResponse updatePosts(@RequestBody UpdatePostsRequest request, @PathVariable("postId") Long id){
        return postsService.update(request,id);
    }

    @DeleteMapping("posts/{postId}")
    public ResponseEntity<?> deletePosts(@PathVariable("postId") Long id){
        postsService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
