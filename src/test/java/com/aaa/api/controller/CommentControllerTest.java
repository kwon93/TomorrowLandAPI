package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.dto.request.CreateCommentRequest;
import com.aaa.api.dto.request.DeleteCommentRequest;
import com.aaa.api.dto.request.UpdateCommentRequest;
import com.aaa.api.dto.response.UpdateCommentResponse;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.service.CommentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {

    private static Posts post = null;

    @BeforeAll
    static void beforeAll() {
         post = Posts.builder()
                .id(1L)
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("createComment(): 댓글 작성 요청에 성공해 http status 201 을 응답한다.")
    void test1() throws Exception {
        //given
        final String content = "답변내용~";
        final String password = "1234";
        final String username = "kwon";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .name(username)
                .content(content)
                .password(password)
                .build();

        Comment response = Comment.builder()
                .username(username)
                .password(password)
                .content(content)
                .build();

        given(commentService.create(anyLong(),any(CreateCommentRequest.class))).willReturn(response);

        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.name").value(username))
                .andDo(print());

        verify(commentService, times(1)).create(anyLong(), refEq(request,"name","password","content"));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("createComment(): 댓글 내용이 500글자 초과시 http status 400을 응답한다.")
    void test2() throws Exception {
        //given
        final String password = "1234";
        final String invalidContent = IntStream.range(0, 501)
                .mapToObj(i -> "" + i)
                .collect(Collectors.joining());
        final String username = "kwon";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .name(username)
                .content(invalidContent)
                .password(password)
                .build();



        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.content").value("답변은 500자 이하로 작성해주세요."))
                .andDo(print());

    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("createComment(): 댓글 작성 암호가 4~12 범위를 벗어날경우 http status 400을 응답한다.")
    void test3() throws Exception {
        //given
        final String password = "12";
        final String content = "답변내용~";
        final String username = "kwon";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .name(username)
                .content(content)
                .password(password)
                .build();



        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.password").value("비밀번호는 4글자 이상 12글자 이하로 입력해주세요."))
                .andDo(print());

    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("createComment(): 댓글 작성자명 10글자 범위를 벗어날경우 http status 400을 응답한다.")
    void test4() throws Exception {
        //given
        final String password = "1234";
        final String content = "답변내용~";
        final String username = "황금독수리온세상을놀라게하다";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .name(username)
                .content(content)
                .password(password)
                .build();

        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.name").value("작성자명은 3글자 이상 10글자 이하로 입력해주세요."))
                .andDo(print());

    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("updateComment(): 댓글 수정 요청에 성공해 http status 204를 응답받아야한다.")
    void test5() throws Exception {
        //given
        Comment comment = Comment.builder()
                .id(1L)
                .content("원본 댓글 내용")
                .password("1234")
                .username("kown")
                .build();

        Comment updatedComment = Comment.builder()
                .id(1L)
                .content("수정된 댓글 내용")
                .password("1234")
                .username("kown")
                .build();


        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("수정된 댓글 내용")
                .password("1234")
                .build();


        given(commentService.update(anyLong(), any(UpdateCommentRequest.class))).willReturn(updatedComment);

        // when
        mockMvc.perform(patch("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.content").value("수정된 댓글 내용"));


        verify(commentService, times(1)).update(anyLong(), refEq(request));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("updateComment(): 댓글 수정 요청에 댓글이 500초과시 http status 400 응답을 받아야한다.")
    void test6() throws Exception {
        //given
        final String invalidContent = IntStream.range(0, 501)
                .mapToObj(i -> "" + i)
                .collect(Collectors.joining());

        Comment comment = Comment.builder()
                .id(1L)
                .content(invalidContent)
                .password("1234")
                .username("kown")
                .build();

        Comment request = Comment.builder()
                .id(1L)
                .content(invalidContent)
                .password("1234")
                .username("kown")
                .build();
        // when
        mockMvc.perform(patch("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.content").value("답변은 500자 이하로 작성해주세요."));
    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("deleteComment(): 댓글 삭제 요청에 성공해 http status 201을 응답받아야한다.")
    void test7() throws Exception {
        //given
        final String content = "답변내용~";
        final String password = "1234";
        final String username = "kwon";

        Comment comment = Comment.builder()
                .id(1L)
                .content("comment~~~~~~~~~~~~~~~")
                .password("1234")
                .username("kown")
                .build();

        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .password("1234")
                .build();


        // when
        mockMvc.perform(post("/api/comment/{commentId}/delete", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).delete(anyLong(), refEq(request));
    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("deleteComment(): 댓글 삭제 요청시 비밀번호가 틀릴 경우 InvalidCommentPasswordException()을 응답받아야한다.")
    void test8() throws Exception {
        //given
        final String content = "답변내용~";
        final String password = "1234";
        final String username = "kwon";

        Comment comment = Comment.builder()
                .id(1L)
                .content("comment~~~~~~~~~~~~~~~")
                .password("123444")
                .username("kown")
                .build();

        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .password("1234")
                .build();

        doThrow(new InvalidCommentPassword()).when(commentService).delete(anyLong(),any(DeleteCommentRequest.class));


        // when
        mockMvc.perform(post("/api/comment/{commentId}/delete", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("잘못된 비밀번호 입니다."))
                .andDo(print());

    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getAllComment(): 댓글 전체 조회 요청에 성공해 http status 200을 응답한다.")
    void test9() throws Exception{
        //given
        Posts post = Posts.builder()
                .id(1L)
                .content("글 내용")
                .title("글 제목")
                .postsCategory(PostsCategory.DEV)
                .build();
        List<Comment> comments = IntStream.range(0, 4).mapToObj(i ->
                Comment.builder()
                        .posts(post)
                        .content("댓글" + i)
                        .password("123456")
                        .username("kwon")
                        .build()).toList();

        given(commentService.getAll(anyLong())).willReturn(comments);

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{postsId}/comment", post.getId()).with(csrf()));

        //then
        result.andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).getAll(anyLong());
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getAllComment(): 댓글 전체 조회 요청시 없는 게시물인 경우 http status 400을 응답한다.")
    void test10() throws Exception{
        //given
        List<Comment> comments = IntStream.range(0, 4).mapToObj(i ->
                Comment.builder()
                        .posts(post)
                        .content("댓글" + i)
                        .password("123456")
                        .username("kwon")
                        .build()).toList();

        doThrow(new PostNotfound()).when(commentService).getAll(anyLong());

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{postsId}/comment",999L).with(csrf()));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("존재하지않는 게시물입니다."))
                .andDo(print());
    }



}