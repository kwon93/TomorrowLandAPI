package com.aaa.api.service;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.service.dto.CommentNoticeServiceDto;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final CommentNotificationService notificationService;

    @Transactional
    public PostCommentResponse create(final CreateCommentServiceRequest serviceRequest) throws JsonProcessingException {
        final Posts commentedPosts =
                postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);
        final Users writeByUser
                = usersRepository.findById(serviceRequest.getUsersId()).orElseThrow(UserNotFound::new);

        final Comment comment = serviceRequest.toEntity(commentedPosts, writeByUser);
        final Comment savedComment = commentRepository.save(comment);

        publishingNoticeToRedis(writeByUser, commentedPosts);
        return PostCommentResponse.of(savedComment);
    }

    private void publishingNoticeToRedis(Users writeByUser, Posts commentedPosts) throws JsonProcessingException {
        CommentNoticeServiceDto commentNotice = CommentNoticeServiceDto
                .builder()
                .commenter(writeByUser.getName())
                .postWriterId(commentedPosts.getUserId())
                .postName(commentedPosts.getTitle())
                .postsId(commentedPosts.getId())
                .build();
        notificationService.publishCommentNotice(commentNotice);
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
    public List<CommentsResponse> getAllComments(GetAllCommentsServiceDto serviceRequest) {
        postsRepository.findById(serviceRequest.getPostsId()).orElseThrow(PostNotfound::new);

        return commentRepository.getCommentListByQueryDSL(serviceRequest.getPostsId()).stream()
                .map(CommentsResponse::new)
                .toList();
    }


    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId).
                orElseThrow(CommentNotFound::new);
    }
}

