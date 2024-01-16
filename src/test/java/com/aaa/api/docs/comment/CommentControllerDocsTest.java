package com.aaa.api.docs.comment;

import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerDocsTest extends RestDocsSupport {

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
    @DisplayName("RestDocs: 댓글 작성 요청 API")
    void test1() throws Exception {
        //given
        final String content = "답변내용~";
        final String password = "1234";
        final String username = "kwon";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .username(username)
                .content(content)
                .password(password)
                .build();

        CreateCommentServiceRequest serviceRequest = CreateCommentServiceRequest.builder()
                .username(username)
                .content(content)
                .password(password)
                .build();

        PostCommentResponse response = PostCommentResponse.builder()
                .username(username)
                .password(password)
                .content(content)
                .build();

        given(commentService.create(any(CreateCommentServiceRequest.class))).willReturn(response);

        // when
        mockMvc.perform(post("/api/posts/{postsId}/comment", post.getId())
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.username").value(username))
                .andDo(print())
                .andDo(document("comment-create",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("작성한 사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("작성한 사용자 비밀번호"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("작성한 댓글 내용")
                        )
                ));

    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 댓글 수정 요청 API")
    void test5() throws Exception {
        //given
        Comment comment = Comment.builder()
                .id(1L)
                .content("원본 댓글 내용")
                .password("1234")
                .username("kown")
                .build();

        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("수정된 댓글 내용")
                .password("1234")
                .build();

        UpdateCommentServiceRequest serviceRequest = UpdateCommentServiceRequest.builder()
                .content("수정된 댓글 내용")
                .password("1234")
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
                .andExpect(jsonPath("$.content").value("수정된 댓글 내용"))
                .andDo(print())
                .andDo(document("comment-update",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("수정할 댓글 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 댓글 내용")
                        )

                        ));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 댓글 삭제 요청 API")
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

        DeleteCommentServiceRequest serviceRequest = DeleteCommentServiceRequest.builder()
                .password("1234")
                .build();


        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/comment/{commentId}/delete", comment.getId())
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("comment-delete",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("삭제할 댓글 번호")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("댓글 비밀번호")
                        )
                ));

    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 모든 댓글 조회 요청 API")
    void test9() throws Exception{
        //given
        Posts post = Posts.builder()
                .id(1L)
                .content("글 내용")
                .title("글 제목")
                .postsCategory(PostsCategory.DEV)
                .build();
        List<Comment> comments = IntStream.range(0, 3).mapToObj(i ->
                Comment.builder()
                        .posts(post)
                        .content("댓글" + i)
                        .password("123456")
                        .username("kwon")
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()).toList();

        given(commentService.getAll(anyLong())).willReturn(comments);

        // when
        ResultActions result =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{postsId}/comment",
                                post.getId())
                        .with(csrf().asHeader()));

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-getAll",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postsId").description("댓글을 가져올 해당 게시물 번호")
                        ),
                        responseFields(
                                fieldWithPath("commentResponse").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                fieldWithPath("commentResponse[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("commentResponse[].username").type(JsonFieldType.STRING).description("댓글 작성자"),
                                fieldWithPath("commentResponse[].isRewarded").type(JsonFieldType.STRING).description("댓글 보상 여부"),
                                fieldWithPath("commentResponse[].regDate").type(JsonFieldType.STRING).description("댓글 작성 날짜"),
                                fieldWithPath("commentResponse[].modDate").type(JsonFieldType.STRING).description("댓글 수정 날짜")
                        )
                        ));

    }

}
