package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.CommentResult;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import com.aaa.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/posts/{postsId}/comment")
    public ResponseEntity<PostCommentResponse> createComment(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal,
                                                             @PathVariable("postsId") final Long postsId,
                                                             @RequestBody @Validated final CreateCommentRequest request){

        final PostCommentResponse response = commentService.create(request.toServiceDto(postsId, userPrincipal.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postsId}/comment")
    public ResponseEntity<CommentResult<CommentsResponse>> getAllComment(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal,
                                                                         @PathVariable("postsId") final Long postsId) {
        if (Optional.ofNullable(userPrincipal).isPresent()){
            Long userId = userPrincipal.getUserId();
            List<CommentsResponse> responses = commentService.getAllWithPrincipal(new GetAllCommentsServiceDto(postsId, userId));
            return ResponseEntity.ok(new CommentResult<CommentsResponse>(responses));
        }

        List<CommentsResponse> responses = commentService.getAllNoPrincipal(new GetAllCommentsServiceDto(postsId, null));
        return ResponseEntity.ok(new CommentResult<CommentsResponse>(responses));
    }



    @PatchMapping("/comment/{commentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#commentId, 'PATCH')")
    public ResponseEntity<UpdateCommentResponse> updateComment(
                                            @PathVariable("commentId")final Long commentId,
                                            @RequestBody @Validated final UpdateCommentRequest request){
        final UpdateCommentResponse response = commentService.update(request.toServiceDto(commentId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    //deleteMapping 요청시 @requestBody 로 값을 받기 불가능해짐. -> Post method로 진행.
    @PostMapping("/comment/{commentId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') && hasPermission(#commentId, 'DELETE')")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId")Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
