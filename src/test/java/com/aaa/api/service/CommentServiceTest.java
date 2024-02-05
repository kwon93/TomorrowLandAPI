package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import com.aaa.api.service.dto.response.CommentsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends IntegrationTestSupport {


    @Test
    @DisplayName("create(): 댓글 작성에 성공한다.")
    void test1() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";

        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);

        CreateCommentServiceRequest request = CreateCommentServiceRequest.builder()
                .usersId(userInTest.getId())
                .postsId(postInTest.getId())
                .content(content)
                .build();

        // when
        commentService.create(request);

        //then
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0))
                .extracting("users","content")
                .contains(comments.get(0).getUsers(),content);
    }


    @Test
    @DisplayName("create(): 게시물을 찾을 수 없는 경우 PostNotFound Exception이 발생.")
    void test2() {
        //given
        final String content = "댓글 내용";
        final Long invalidPostId = 99L;

        CreateCommentServiceRequest request = CreateCommentServiceRequest.builder()
                .postsId(invalidPostId)
                .content(content)
                .build();

        // when
        assertThatThrownBy(()-> {
            commentService.create(request);
        }).isInstanceOf(PostNotfound.class).hasMessage("존재하지않는 게시물 참조");
    }


    @Test
    @DisplayName("update(): 댓글 수정에 성공한다.")
    void test3() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        final String updateContent = "이 댓글 제가 수정합니다.";
        Comment commentInTest = createCommentInTest(name,content,password);

        UpdateCommentServiceRequest request = UpdateCommentServiceRequest.builder()
                .commentId(commentInTest.getId())
                .content(updateContent)
                .build();

        // when
        commentService.update(request);

        //then
        Comment updatedComment = commentRepository.findById(commentInTest.getId()).orElseThrow(CommentNotFound::new);
        assertThat(updatedComment.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("delete(): 댓글 삭제에 성공한다.")
    void test5() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        Comment commentInTest = createCommentInTest(name,content,password);



        // when
        commentService.delete(commentInTest.getId());

        //then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("getAllNoPrincipal(): 비로그인시 해당 게시물의 댓글들을 작성 시간 오름차순으로 가져온다.")
    void test7() {
        //given
        Users userInTest = createUserInTest();

        Posts postInTest = createPostInTest(userInTest);
        LocalDateTime now = LocalDateTime.now();

        List<Comment> comments = IntStream.range(0, 3).mapToObj(i ->
                Comment.builder()
                        .users(userInTest)
                        .posts(postInTest)
                        .content("댓글" + i)
                        .regDate(now)
                        .build())
                .toList();

        commentRepository.saveAll(comments);

        // when
        List<CommentsResponse> commentsByTest = commentService.getAllNoPrincipal(new GetAllCommentsServiceDto(postInTest.getId(), null));
        //then
        assertThat(commentsByTest.size()).isEqualTo(3);
        assertThat(commentsByTest.get(0).isModifiable()).isFalse();
        assertThat(comments.get(0))
                .extracting("content","users")
                .contains("댓글0",comments.get(0).getUsers());
    }


    @Test
    @DisplayName("getAllNoPrincipal(): 로그인시 해당 게시물의 댓글들 가져온다.")
    void test8() {
        //given
        final Users userInTest = createUserInTest();
        final Posts postInTest = createPostInTest(userInTest);
        final LocalDateTime now = LocalDateTime.now();
        final Users modifiableUser = Users.builder()
                .id(99L)
                .email("modi@test.com")
                .password("password")
                .name("modi")
                .build();
        usersRepository.save(modifiableUser);

        List<Comment> comments = IntStream.range(0, 3).mapToObj(i ->
                Comment.builder()
                        .users(userInTest)
                        .posts(postInTest)
                        .content("댓글" + i)
                        .regDate(now)
                        .build())
                .toList();
        commentRepository.saveAll(comments);

        Comment modifiableComment = Comment.builder()
                .users(modifiableUser)
                .posts(postInTest)
                .content("modifiable")
                .regDate(now)
                .build();
        commentRepository.save(modifiableComment);

        // when
        List<CommentsResponse> commentsByTest = commentService.getAllWithPrincipal(new GetAllCommentsServiceDto(postInTest.getId(), modifiableUser.getId()));
        //then
        assertThat(commentsByTest.size()).isEqualTo(4);
        assertThat(commentsByTest.get(0).isModifiable()).isFalse();
        assertThat(commentsByTest.get(3).isModifiable()).isTrue();
        assertThat(commentsByTest.get(3).getContent()).isEqualTo("modifiable");
    }

    private Comment createCommentInTest(String name, String content, String password) {
        Comment comment = Comment.builder()
                .content(content)
                .build();
        return commentRepository.save(comment);
    }



}