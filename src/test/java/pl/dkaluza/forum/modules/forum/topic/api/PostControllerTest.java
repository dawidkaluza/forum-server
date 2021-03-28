package pl.dkaluza.forum.modules.forum.topic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.modules.forum.topic.api.TopicResultMatchers.expectPost;
import static pl.dkaluza.forum.modules.forum.topic.api.TopicResultMatchers.expectPostsPage;
import static pl.dkaluza.forum.modules.forum.topic.api.TopicSnippets.*;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectError;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectFieldError;
import static pl.dkaluza.forum.utils.mockmvc.PagedResultMatchers.expectEmptyPage;
import static pl.dkaluza.forum.utils.restdocs.PaginationUtils.pageParamDescriptor;
import static pl.dkaluza.forum.utils.restdocs.PaginationUtils.sizeParamDescriptor;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class})
public class PostControllerTest {
    @RegisterExtension
    final RestDocumentationExtension restDoc = new RestDocumentationExtension("target/generated-snippets/forum/topic");

    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public PostControllerTest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(springSecurity())
            .apply(
                documentationConfiguration(restDoc)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build();
    }

//    @Test
//    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
//    public void create_notAuthenticated_returnInsufficientAuthException() throws Exception {
//        //Given
//        Map<String, Object> body = new HashMap<>();
//        body.put("content", "Some post content");
//        body.put("topicId", 1);
//        body.put("authorId", 1);
//
//        //When
//        ResultActions result = mockMvc.perform(
//            post("/post")
//                .content(objectMapper.writeValueAsString(body))
//                .contentType(MediaType.APPLICATION_JSON)
//                .locale(Locale.ENGLISH)
//        );
//
//        //Then
        //TODO
        // java.lang.AssertionError: No value at JSON path "$.status"
        // it happens because mockMvc doesn't use ErrorAttributes component
        // Possible solutions:
        //  - https://github.com/spring-projects/spring-boot/issues/7321
        //  - https://stackoverflow.com/questions/29120948/testing-a-spring-boot-application-with-custom-errorattributes
//        expectError(result, HttpStatus.UNAUTHORIZED);
//
//        //Document
//        result.andDo(document("createPost/notAuthenticated"));
//    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_invalidAuthorAuthenticated_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Some post content");
        body.put("topicId", 1);
        body.put("authorId", 2);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.FORBIDDEN);

        //Document
        result.andDo(document("createPost/invalidAuthorAuthenticated"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyAuthor_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Some post content");
        body.put("topicId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "authorId");

        //Document
        result.andDo(document("createPost/emptyAuthor"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyTopic_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Some post content");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "topicId");

        //Document
        result.andDo(document("createPost/emptyTopic"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_notExistingTopic_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Some post content");
        body.put("topicId", 69);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("createPost/topicNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_closedTopic_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Some post content");
        body.put("topicId", 6);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.LOCKED);

        //Document
        result.andDo(document("createPost/closedTopic"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_nullContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("topicId", 1);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document - below
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyContent_responseWithProperError(String content) throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", content);
        body.put("topicId", 1);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createPost/emptyContent"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooShortContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "ab");
        body.put("topicId", 1);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createPost/tooShortContent"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooLongContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < 1025; i++) {
            contentBuilder.append("a");
        }
        body.put("content", contentBuilder.toString());
        body.put("topicId", 1);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createPost/tooLongContent"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_validRequest_responseWithCreatedPost() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Damn, nice to see you :)");
        body.put("topicId", 1);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectPost(result, 11);

        //Document
        result.andDo(document(
            "createPost/success",
            requestFields(
                fieldWithPath("content").description("Content of created post. Simple message that will be seen while viewing this post"),
                fieldWithPath("topicId").description("Topic id to which the post will be attached"),
                fieldWithPath("authorId").description("Author id. At the moment it's just an id of authenticated user")
            ),
            postResponseFields(),
            postLinks()
        ));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getAll_noPosts_responseWithNoPosts() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectEmptyPage(result);
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getAll_fivePosts_responseWithThesePosts() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/post?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectPostsPage(result, 8, 2);

        //Documentation
        result.andDo(document(
            "getAllPosts/success",
            requestParameters(
                pageParamDescriptor(),
                sizeParamDescriptor()
            ),
            postsPageResponseFields(),
            postsPageLinks()
        ));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void get_notExistingId_responseWithError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/post/34")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("getPost/postNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void get_existingId_responseWithPost() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/post/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectPost(result, 1);

        //Document
        result.andDo(document(
            "getPost/success",
            pathParameters(
                parameterWithName("id").description("Post's id the user want to get")
            ),
            postResponseFields(),
            postLinks()
        ));
    }
}
