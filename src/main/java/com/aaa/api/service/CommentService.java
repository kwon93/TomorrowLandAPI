package com.aaa.api.service;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.dto.request.CreateCommentRequest;
import com.aaa.api.dto.request.DeleteCommentRequest;
import com.aaa.api.dto.request.UpdateCommentRequest;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.CommentRepository;
import com.aaa.api.repository.Posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Comment create(Long postsId, CreateCommentRequest request) {
        Posts posts =
                postsRepository.findById(postsId).orElseThrow(PostNotfound::new);

        String encodedPW = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.of(posts,request,encodedPW);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment update(Long commentId, UpdateCommentRequest request) {
        Comment comment = findCommentById(commentId);

        return comment.updateComment(request);
    }


    @Transactional
    public void delete(Long commentId, DeleteCommentRequest request) {
        Comment comment = findCommentById(commentId);

        if (!passwordEncoder.matches(request.getPassword(),comment.getPassword())){
            throw new InvalidCommentPassword();
        }

        commentRepository.delete(comment);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).
                orElseThrow(CommentNotFound::new);
    }
}

