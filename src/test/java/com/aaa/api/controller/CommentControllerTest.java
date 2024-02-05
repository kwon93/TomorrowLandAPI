package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import com.aaa.api.exception.InvalidCommentPassword;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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
    @CustomMockUser
    @DisplayName("createComment(): 댓글 작성 요청에 성공해 http status 201 을 응답한다.")
    void test1() throws Exception {
        //given
        final String content = "답변내용~";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .content(content)
                .build();

        PostCommentResponse response = PostCommentResponse.builder()
                .content(content)
                .build();

        given(commentService.create(any(CreateCommentServiceRequest.class))).willReturn(response);

        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content))
                .andDo(print());

        verify(commentService, times(1)).create(any(CreateCommentServiceRequest.class));
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
                .content(invalidContent)
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
    @DisplayName("updateComment(): 댓글 수정 요청에 성공해 http status 204를 응답받아야한다.")
    void test5() throws Exception {
        //given
         Comment comment = Comment.builder()
                .id(1L)
                .content("원본 댓글 내용")
                .build();

        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("수정된 댓글 내용")
                .build();

        UpdateCommentServiceRequest serviceRequest = UpdateCommentServiceRequest.builder()
                .content("수정된 댓글 내용")
                .build();

        UpdateCommentResponse updateComment = UpdateCommentResponse.builder()
                .content("수정된 댓글 내용")
                .build();

        given(commentService.update(any(UpdateCommentServiceRequest.class))).willReturn(updateComment);

        // when
        mockMvc.perform(patch("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.content").value("수정된 댓글 내용"));


        verify(commentService, times(1)).update(any(UpdateCommentServiceRequest.class));
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
                .content("내용입니다.")
                .build();

         UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content(invalidContent)
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
                .build();

        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .password("1234")
                .build();

        DeleteCommentServiceRequest serviceRequest = DeleteCommentServiceRequest.builder()
                .password("1234")
                .build();


        // when
        mockMvc.perform(delete("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).delete(anyLong());
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
                .build();

        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .password("1234")
                .build();

        doThrow(new InvalidCommentPassword()).when(commentService).delete(anyLong());


        // when
        mockMvc.perform(delete("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("비밀번호 입력 실패"))
                .andDo(print());

    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getAllComment(): 비로그인시의 댓글 전체 조회 요청에 성공해 http status 200을 응답한다.")
    void test9() throws Exception{
        //given
        Posts post = Posts.builder()
                .id(1L)
                .content("글 내용")
                .title("글 제목")
                .postsCategory(PostsCategory.DEV)
                .build();
        List<Comment> comments = LongStream.range(0, 3).mapToObj(i ->
                Comment.builder()
                        .id(i)
                        .users(Users.builder().id(i).email("test@naver.com").name("kwon").build())
                        .posts(post)
                        .content("댓글" + i)
                        .build()).toList();

        List<CommentsResponse> responses = comments.stream()
                .map(comment -> new CommentsResponse(comment, false)).toList();
        given(commentService.getAllNoPrincipal(any(GetAllCommentsServiceDto.class))).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{postsId}/comment", post.getId()).with(csrf()));

        //then
        result.andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).getAllNoPrincipal(any(GetAllCommentsServiceDto.class));
    }

    @Test
    @CustomMockUser
    @DisplayName("getAllComment(): 로그인시의 댓글 전체 조회 요청에 성공해 http status 200을 응답한다.")
    void test11() throws Exception{
        //given
        Posts post = Posts.builder()
                .id(1L)
                .content("글 내용")
                .title("글 제목")
                .postsCategory(PostsCategory.DEV)
                .build();
        List<Comment> comments = LongStream.range(0, 2).mapToObj(i ->
                Comment.builder()
                        .id(i)
                        .users(Users.builder().id(i).email("test@naver.com").name("kwon").build())
                        .posts(post)
                        .content("댓글" + i)
                        .build()).toList();

        List<CommentsResponse> responses = comments.stream()
                .map(comment -> new CommentsResponse(comment, false)).toList();

        responses.forEach(response -> {response.setModifiable(true);});

        given(commentService.getAllWithPrincipal(any(GetAllCommentsServiceDto.class))).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{postsId}/comment", post.getId()).with(csrf()));

        //then
        result.andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).getAllWithPrincipal(any(GetAllCommentsServiceDto.class));
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
                        .build()).toList();

        doThrow(new PostNotfound()).when(commentService).getAllNoPrincipal(any(GetAllCommentsServiceDto.class));

        // when
        ResultActions result = mockMvc.perform(get("/api/posts/{postsId}/comment",999L).with(csrf().asHeader()));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("존재하지않는 게시물 참조"))
                .andDo(print());
    }



}