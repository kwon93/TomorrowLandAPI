package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.CreatePostsRequest;
import com.aaa.api.controller.dto.request.PostSearch;
import com.aaa.api.controller.dto.request.UpdatePostsRequest;
import com.aaa.api.domain.Posts;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<PostsResponse> createPosts(@AuthenticationPrincipal CustomUserPrincipal userPrincipal,
                                                     @RequestBody @Validated final CreatePostsRequest request){

        final PostsResponse posts = postsService.create(request.toServiceDto(userPrincipal));
        return ResponseEntity.status(HttpStatus.CREATED).body(posts);
    }

    @GetMapping("posts")
    public ResponseEntity<PostsResult<PostsResponse>> getAllPosts(final PostSearch postSearch){
        final List<PostsResponse> responses = postsService.getPage(postSearch.toServiceDto()).stream()
                .map(PostsResponse::new)
                .toList();
        return ResponseEntity.ok(new PostsResult<>(responses, postsService.getAll()));
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<PostsResponse> getOnePosts(@PathVariable("postId") final Long id ){
        PostsResponse response = postsService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("posts/{postsId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#postsId, 'Posts', 'PATCH')")
    public ResponseEntity<PostsResponse> updatePosts(@RequestBody final UpdatePostsRequest request,
                                     @PathVariable("postsId") final Long postsId){
        postsService.update(request.toServiceDto(postsId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("posts/{postsId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#postsId, 'Posts', 'DELETE')")
    public ResponseEntity<Void> deletePosts(@PathVariable("postsId") final Long postsId){
        postsService.delete(postsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
