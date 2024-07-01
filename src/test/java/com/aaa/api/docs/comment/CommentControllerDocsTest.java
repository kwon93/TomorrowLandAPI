package com.aaa.api.docs.comment;

import com.aaa.api.config.RestDocMockUser;
import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.DeleteCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import com.aaa.api.service.dto.request.GetAllCommentsServiceDto;
import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import com.aaa.api.service.dto.response.CommentsResponse;
import com.aaa.api.service.dto.response.PostCommentResponse;
import com.aaa.api.service.dto.response.UpdateCommentResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    @RestDocMockUser
    @DisplayName("RestDocs: 댓글 작성 요청 API")
    void test1() throws Exception {
        //given
        final String content = "답변내용~";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .content(content)
                .build();

        CreateCommentServiceRequest serviceRequest = CreateCommentServiceRequest.builder()
                .content(content)
                .build();

        PostCommentResponse response = PostCommentResponse.builder()
                .content(content)
                .build();

        given(commentService.create(any(CreateCommentServiceRequest.class))).willReturn(response);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/posts/{postsId}/comment", post.getId())
                        .with(csrf().asHeader())
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(content))
                .andDo(print())
                .andDo(document("comment-create",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postsId").description("댓글을 등록할 해당 게시물 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                        ),
                        responseFields(
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
                .build();

        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("수정된 댓글 내용")
                .build();


        UpdateCommentResponse updateComment = UpdateCommentResponse.builder()
                .content("수정된 댓글 내용")
                .build();

        given(commentService.update(any(UpdateCommentServiceRequest.class))).willReturn(updateComment);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/comment/{commentId}", comment.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.content").value("수정된 댓글 내용"))
                .andDo(print())
                .andDo(document("comment-update",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("수정할 댓글 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용")
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
        Comment comment = Comment.builder()
                .id(1L)
                .content("comment~~~~~~~~~~~~~~~")
                .build();

        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .password("1234")
                .build();

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/comment/{commentId}", comment.getId())
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
                        )
                ));

    }

    @Test
    @RestDocMockUser
    @DisplayName("RestDocs: 모든 댓글 조회 요청 API")
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
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .content("댓글" + i)
                        .build()).toList();

        List<CommentsResponse> responses = comments.stream()
                .map(CommentsResponse::new).toList();
        given(commentService.getAllComments(any(GetAllCommentsServiceDto.class))).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{postsId}/comment", post.getId()).with(csrf()));

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
                                fieldWithPath("commentResponse[].id").type(JsonFieldType.NUMBER).description("댓글 번호"),
                                fieldWithPath("commentResponse[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("commentResponse[].userName").type(JsonFieldType.STRING).description("댓글 작성자 이름"),
                                fieldWithPath("commentResponse[].userEmail").type(JsonFieldType.STRING).description("댓글 작성자 이메일"),
                                fieldWithPath("commentResponse[].userId").type(JsonFieldType.NUMBER).description("댓글 작성자 번호"),
                                fieldWithPath("commentResponse[].isRewarded").type(JsonFieldType.STRING).description("댓글 보상 여부"),
                                fieldWithPath("commentResponse[].regDate").type(JsonFieldType.STRING).description("댓글 작성 날짜"),
                                fieldWithPath("commentResponse[].modDate").type(JsonFieldType.STRING).description("댓글 수정 날짜")
                        )
                        ));

    }

}
