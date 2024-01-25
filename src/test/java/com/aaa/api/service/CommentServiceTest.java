package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
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
        final String password = "123455";

        Posts postInTest = createPostInTest();

        CreateCommentServiceRequest request = CreateCommentServiceRequest.builder()
                .postsId(postInTest.getId())
                .username(name)
                .content(content)
                .password(password)
                .build();

        // when
        commentService.create(request);

        //then
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments.size()).isEqualTo(1);
        assertThat(passwordEncoder.matches(password, comments.get(0).getPassword())).isTrue();
        assertThat(comments.get(0))
                .extracting("username","content")
                .contains(name,content);

    }


    @Test
    @DisplayName("create(): 게시물을 찾을 수 없는 경우 PostNotFound Exception이 발생.")
    void test2() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        final Long invalidPostId = 99L;

        CreateCommentServiceRequest request = CreateCommentServiceRequest.builder()
                .postsId(invalidPostId)
                .username(name)
                .content(content)
                .password(password)
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
                .password(password)
                .build();

        // when
        commentService.update(request);

        //then
        Comment updatedComment = commentRepository.findById(commentInTest.getId()).orElseThrow(CommentNotFound::new);
        assertThat(updatedComment.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("update(): 댓글 수정요청시 비밀번호가 틀릴 경우 InvalidCommentPassword 발생")
    void test4() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        final String invalidPassword = "invalid";
        final String updateContent = "이 댓글 제가 수정합니다.";
        Comment commentInTest = createCommentInTest(name,content,password);

        UpdateCommentServiceRequest request = UpdateCommentServiceRequest.builder()
                .commentId(commentInTest.getId())
                .content(updateContent)
                .password(invalidPassword)
                .build();

        // when
        assertThatThrownBy(()->{
            commentService.update(request);})
                .isInstanceOf(InvalidCommentPassword.class)
                .hasMessage("비밀번호 입력 실패");

    }


    @Test
    @DisplayName("delete(): 댓글 삭제에 성공한다.")
    void test5() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        Comment commentInTest = createCommentInTest(name,content,password);

        DeleteCommentServiceRequest request = DeleteCommentServiceRequest.builder()
                .commentId(commentInTest.getId())
                .password(password)
                .build();


        // when
        commentService.delete(request);

        //then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("delete(): 댓글 삭제 요청시 비밀번호가 틀릴 경우 InvalidCommentPassword 발생") void test6() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        final String invalidPassword = "invalid";

        Comment commentInTest = createCommentInTest(name,content,password);

        DeleteCommentServiceRequest request = DeleteCommentServiceRequest.builder()
                .commentId(commentInTest.getId())
                .password(invalidPassword)
                .build();

        // when
        assertThatThrownBy(()->{
            commentService.delete(request);})
                .isInstanceOf(InvalidCommentPassword.class)
                .hasMessage("비밀번호 입력 실패");

    }


    @Test
    @DisplayName("getAll(): 해당 게시물의 댓글들을 작성 시간 오름차순으로 가져온다.")
    void test7() {
        //given
        final String name = "kwon";
        final String content = "댓글 내용";
        final String password = "123455";
        final String invalidPassword = "invalid";

        Posts postInTest = createPostInTest();
        LocalDateTime now = LocalDateTime.now();

        List<Comment> comments = IntStream.range(0, 3).mapToObj(i ->
                Comment.builder()
                        .posts(postInTest)
                        .content("댓글" + i)
                        .password("123456")
                        .username("kwon")
                        .regDate(now)
                        .build()).toList();

        commentRepository.saveAll(comments);

        // when
        List<Comment> commentsByTest = commentService.getAll(postInTest.getId());
        //then
        assertThat(commentsByTest.size()).isEqualTo(3);
        assertThat(comments.get(0))
                .extracting("content","username")
                .contains("댓글0","kwon");

    }



    private Comment createCommentInTest(String name, String content, String password) {
        Comment comment = Comment.builder()
                .username(name)
                .content(content)
                .password(passwordEncoder.encode(password))
                .build();
        return commentRepository.save(comment);
    }



}