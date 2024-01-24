package com.aaa.api.docs.posts;

import com.aaa.api.config.RestDocMockUser;
import com.aaa.api.controller.dto.request.CreatePostsRequest;
import com.aaa.api.controller.dto.request.UpdatePostsRequest;
import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import com.aaa.api.service.dto.request.PostSearchForService;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import com.aaa.api.service.dto.response.PostsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostsControllerDocsTest extends RestDocsSupport {

    @Test
    @RestDocMockUser
    @DisplayName("RestDocs: 신규 게시물을 등록 요청 API")
    void createPosts() throws Exception{
        //when
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .imagePath("image/test.png")
                .build();

        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .imagePath("image/test.png")
                .build();


        given(postsService.create(any(CreatePostsServiceRequest.class))).willReturn(response);

        // expected
        mockMvc.perform(post("/api/posts")
                        .with(csrf().asHeader())
                        .with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("posts-create",
                                preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING) //enum은 String
                                                .description("게시물 제목"),
                                        fieldWithPath("content")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 내용"),
                                        fieldWithPath("category")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 카테고리 [DEV, MEDIA, LIFE, ETC]"),
                                        fieldWithPath("imagePath")
                                                .type(JsonFieldType.STRING)
                                                .description("S3 이미지 경로")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시글 번호"),
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 제목"),
                                        fieldWithPath("content")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 내용"),
                                        fieldWithPath("category")
                                                .type(JsonFieldType.STRING)
                                                .description("게시글 카테고리"),
                                        fieldWithPath("viewCount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시물 조회수"),
                                        fieldWithPath("likeCount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시글 추천수"),
                                        fieldWithPath("imagePath")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 이미지 경로"))
                                ));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 게시물 페이지 목록 요청 API")
    void test3() throws Exception {
        //given
        List<Posts> postsList = LongStream.range(1, 11)
                .mapToObj(i -> Posts.builder()
                        .id(i)
                        .title("제목" + i)
                        .content("내용" + i)
                        .postsCategory(PostsCategory.DEV)
                        .imagePath("image/test.png")
                        .build())
                .toList();


        PostSearchForService postSearch = PostSearchForService.builder()
                .page(1)
                .size(10)
                .build();

        given(postsService.getAll(any(PostSearchForService.class))).willReturn(postsList);

        // when
        mockMvc.perform(get("/api/posts?page=1&size=10")
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(postSearch))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("posts-getPage",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 전체 글 개수"),
                                fieldWithPath("offset").type(JsonFieldType.NUMBER).description("페이지 시작 위치")
                        ),
                        responseFields(
                                fieldWithPath("postsResponses").type(JsonFieldType.ARRAY).description("게시글 응답 목록"),
                                fieldWithPath("postsResponses[].id").type(JsonFieldType.NUMBER).description("게시물 번호"),
                                fieldWithPath("postsResponses[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("postsResponses[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("postsResponses[].category").type(JsonFieldType.STRING).description("게시물 카테고리"),
                                fieldWithPath("postsResponses[].imagePath").type(JsonFieldType.STRING).description("게시물 이미지 경로"),
                                fieldWithPath("postsResponses[].viewCount").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("postsResponses[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수")
                        )

                ));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 게시물 단건 조회 요청 API")
    void test5() throws Exception {
        //given
        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .imagePath("image/test.png")
                .build();

        given(postsService.getOne(response.getId())).willReturn(response);

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/{postId}",response.getId()).with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("안녕하세요."))
                .andExpect(jsonPath("$.category").value(PostsCategory.DEV.toString()))
                .andDo(print())
                        .andDo(document("posts-getPosts",
                                preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("조회할 게시물 번호")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시글 번호"),
                                        fieldWithPath("title")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 제목"),
                                        fieldWithPath("content")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 내용"),
                                        fieldWithPath("category")
                                                .type(JsonFieldType.STRING)
                                                .description("게시글 카테고리"),
                                        fieldWithPath("viewCount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시물 조회수"),
                                        fieldWithPath("likeCount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("게시글 추천수"),
                                        fieldWithPath("imagePath")
                                                .type(JsonFieldType.STRING)
                                                .description("게시물 이미지 경로"))

                                ));
        verify(postsService, times(1)).getOne(response.getId());
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 게시물 수정 요청 API")
    void test6() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .content(updateContent)
                .category(updateCategory)
                .build();

        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title(updateTitle)
                .content(updateContent)
                .category(updateCategory)
                .category(PostsCategory.DEV)
                .imagePath("image/test.png")
                .build();

        given(postsService.update(any(UpdatePostsServiceRequest.class))).willReturn(response);

        // when then
        mockMvc.perform(patch("/api/posts/{postId}",response.getId())
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTitle))
                .andExpect(jsonPath("$.content").value(updateContent))
                .andDo(print())
                .andDo(document("posts-update",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 게시물 내용"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("수정할 게시물 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("게시글 번호"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("게시물 제목"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("게시물 내용"),
                                fieldWithPath("category")
                                        .type(JsonFieldType.STRING)
                                        .description("게시글 카테고리"),
                                fieldWithPath("viewCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("게시물 조회수"),
                                fieldWithPath("likeCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("게시글 추천수"),
                                fieldWithPath("imagePath")
                                        .type(JsonFieldType.STRING)
                                        .description("게시물 이미지 경로"))
                        )
                );
    }


    @Test
    @RestDocMockUser
    @DisplayName("RestDocs: 게시물 삭제 요청 API")
    void test8() throws Exception {
        //given
        final Long postId = 1L;

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/posts/{postId}",postId).with(csrf().asHeader()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("posts-delete",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("postId").description("삭제할 게시물 번호"))
                ));
    }
}
