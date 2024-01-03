package com.aaa.api.controller;

import com.aaa.api.domain.Comment;
import com.aaa.api.dto.request.DeleteCommentRequest;
import com.aaa.api.dto.request.CreateCommentRequest;
import com.aaa.api.dto.request.UpdateCommentRequest;
import com.aaa.api.dto.response.CommentResponse;
import com.aaa.api.dto.response.UpdateCommentResponse;
import com.aaa.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postsId}/comment")
    public ResponseEntity<?> createComment(@PathVariable("postsId") Long postsId,
                                        @RequestBody @Validated CreateCommentRequest request){

        Comment comment = commentService.create(postsId, request);
        CommentResponse response = CommentResponse.of(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId")Long commentId,
                                           @RequestBody @Validated UpdateCommentRequest request){

        Comment updatedComment = commentService.update(commentId, request);
        UpdateCommentResponse response = UpdateCommentResponse.of(updatedComment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    //deleteMapping 요청시 @requestBody 로 값을 받기 불가능해짐. -> Post method로 진행.
    @PostMapping("/comment/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId")Long commentId,
                                           @RequestBody @Validated DeleteCommentRequest request){

        commentService.delete(commentId,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
