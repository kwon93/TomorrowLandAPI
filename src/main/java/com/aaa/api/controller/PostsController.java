package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.CreatePostsRequest;
import com.aaa.api.controller.dto.request.PostSearch;
import com.aaa.api.controller.dto.request.UpdatePostsRequest;
import com.aaa.api.service.dto.response.PostsResponse;
import com.aaa.api.service.dto.response.PostsResult;
import com.aaa.api.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;


    @PostMapping("posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PostsResponse> createPosts(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                     @RequestBody @Validated CreatePostsRequest request){

        PostsResponse posts = postsService.create(request.toServiceDto(userPrincipal));
        return ResponseEntity.status(HttpStatus.CREATED).body(posts);
    }

    @GetMapping("posts")
    public ResponseEntity<PostsResult<PostsResponse>> getAllPosts(PostSearch postSearch){
        List<PostsResponse> responses = postsService.getAll(postSearch.toServiceDto()).stream()
                .map(PostsResponse::new)
                .toList();
        return ResponseEntity.ok(new PostsResult<>(responses));
    }

    @GetMapping("posts/{postId}")
    public PostsResponse getOnePosts(@PathVariable("postId") Long id){
        return postsService.getOne(id);
    }

    @PatchMapping("posts/{postId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#postId, 'PATCH')")
    public PostsResponse updatePosts(@RequestBody UpdatePostsRequest request,
                                     @PathVariable("postId") Long postsId){

        return postsService.update(request.toServiceDto(postsId));
    }

    @DeleteMapping("posts/{postId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#postId, 'DELETE')")
    public ResponseEntity<Void> deletePosts(@PathVariable("postId") Long id){
        postsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
