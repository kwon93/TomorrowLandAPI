package com.aaa.api.service;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public PostCommentResponse create(final CreateCommentServiceRequest serviceRequest) {
        final Posts commentedPosts =
                postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);
        final Users writeByUser
                = usersRepository.findById(serviceRequest.getUsersId()).orElseThrow(UserNotFound::new);

        final Comment comment = serviceRequest.toEntity(commentedPosts, writeByUser);
        final Comment savedComment = commentRepository.save(comment);

        return PostCommentResponse.of(savedComment);
    }

    @Transactional
    public UpdateCommentResponse update(final UpdateCommentServiceRequest serviceRequest) {
        final Comment comment = findCommentById(serviceRequest.getCommentId());
        final Comment updatedComment =
                commentRepository.save(serviceRequest.updateComment(comment));

        return UpdateCommentResponse.of(updatedComment);
    }


    @Transactional
    public void delete(final long commentId) {
        final Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentsResponse> getAllWithPrincipal(GetAllCommentsServiceDto serviceRequest) {
        postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);
        Users commentedUser = usersRepository.findById(serviceRequest.getUserId()).orElseThrow(UserNotFound::new);

        List<CommentsResponse> responses = commentRepository.getCommentListByQueryDSL(serviceRequest.getPostsId()).stream()
                .map(comment -> new CommentsResponse(comment, false))
                .toList();

        responses.forEach(response ->
                response.setModifiable(response.getUserName().equals(commentedUser.getName()))
        );
        return responses;
    }

    @Transactional(readOnly = true)
    public List<CommentsResponse> getAllNoPrincipal(GetAllCommentsServiceDto serviceRequest){
            postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);
            return commentRepository.getCommentListByQueryDSL(serviceRequest.getPostsId()).stream()
                    .map(comment -> new CommentsResponse(comment,false))
                    .toList();
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId).
                orElseThrow(CommentNotFound::new);
    }
}

