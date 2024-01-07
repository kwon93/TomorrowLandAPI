package com.aaa.api.service;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.Posts.PostsRepository;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PostCommentResponse create(final CreateCommentServiceRequest serviceRequest) {
        final Posts posts =
                postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);
        final String encodedPassword = passwordEncoder.encode(serviceRequest.getPassword());

        final Comment comment = serviceRequest.toEntity(posts, encodedPassword);
        final Comment savedComment = commentRepository.save(comment);

        return PostCommentResponse.of(savedComment);

    }

    @Transactional
    public UpdateCommentResponse update(final UpdateCommentServiceRequest serviceRequest) {
        Comment comment = findCommentById(serviceRequest.getCommentId());

        if (!passwordEncoder.matches(serviceRequest.getPassword(),comment.getPassword())){
            throw new InvalidCommentPassword();
        }

        Comment updatedComment =
                commentRepository.save(serviceRequest.updateComment(comment));

        return UpdateCommentResponse.of(updatedComment);
    }


    @Transactional
    public void delete(final DeleteCommentServiceRequest serviceRequest) {
        Comment comment = findCommentById(serviceRequest.getCommentId());

        if (!passwordEncoder.matches(serviceRequest.getPassword(),comment.getPassword())){
            throw new InvalidCommentPassword();
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAll(final Long postsId) {
        postsRepository.findById(postsId).orElseThrow(PostNotfound::new);
        return commentRepository.getCommentListByQueryDSL(postsId);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId).
                orElseThrow(CommentNotFound::new);
    }
}

