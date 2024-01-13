package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.CommentResult;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import com.aaa.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postsId}/comment")
    public ResponseEntity<PostCommentResponse> createComment(@PathVariable("postsId") final Long postsId,
                                                             @RequestBody @Validated final CreateCommentRequest request){

        final PostCommentResponse response = commentService.create(request.toServiceDto(postsId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postsId}/comment")
    public ResponseEntity<CommentResult<CommentsResponse>> getAllComment(@PathVariable("postsId") final Long postsId) {
        final List<CommentsResponse> responseList = commentService.getAll(postsId)
                .stream()
                .map(CommentsResponse::new)
                .toList();

        return ResponseEntity.ok(new CommentResult<CommentsResponse>(responseList));
    }


    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(@PathVariable("commentId")final Long commentId,
                                           @RequestBody @Validated final UpdateCommentRequest request){

        final UpdateCommentResponse response = commentService.update(request.toServiceDto(commentId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    //deleteMapping 요청시 @requestBody 로 값을 받기 불가능해짐. -> Post method로 진행.
    @PostMapping("/comment/{commentId}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId")Long commentId,
                                                 @RequestBody @Validated DeleteCommentRequest request){

        commentService.delete(request.toServiceDto(commentId));
        return ResponseEntity.noContent().build();
    }

}
