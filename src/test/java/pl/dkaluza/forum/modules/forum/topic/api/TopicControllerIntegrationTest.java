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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.core.restdocs.RequestsUtils.pageParamDescriptor;
import static pl.dkaluza.forum.core.restdocs.RequestsUtils.sizeParamDescriptor;
import static pl.dkaluza.forum.modules.forum.topic.api.TopicResultMatchers.*;
import static pl.dkaluza.forum.modules.forum.topic.api.TopicSnippets.*;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectError;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectFieldError;
import static pl.dkaluza.forum.utils.mockmvc.PagedResultMatchers.expectEmptyPage;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class})
public class TopicControllerIntegrationTest {
    @RegisterExtension
    final RestDocumentationExtension restDoc = new RestDocumentationExtension("target/generated-snippets/forum/topic");

    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public TopicControllerIntegrationTest(ObjectMapper objectMapper) {
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

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_invalidAuthorAuthenticated_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 2);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.FORBIDDEN);

        //Document
        result.andDo(document("createTopic/invalidAuthorAuthenticated"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyAuthor_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("content", "Hello, below you can write must usable features, let's go!");

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "authorId");

        //Document
        result.andDo(document("createTopic/emptyAuthor"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_nullTitle_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "title");
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "     "})
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyTitle_responseWithProperError(String title) throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "title");

        //Document
        result.andDo(document("createTopic/emptyTitle"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooShortTitle_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "ab");
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "title");

        //Document
        result.andDo(document("createTopic/tooShortTitle"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooLongTitle_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < 193; i++) {
            titleBuilder.append("a");
        }
        body.put("title", titleBuilder.toString());
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "title");

        //Document
        result.andDo(document("createTopic/tooLongTitle"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_nullContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");
    }

    @ParameterizedTest
    @ValueSource(strings = { "  ", "     "})
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_emptyContent_responseWithProperError(String content) throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("content", content);
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createTopic/emptyContent"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooShortContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("content", "ab");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createTopic/tooShortContent"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_tooLongContent_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < 1025; i++) {
            titleBuilder.append("a");
        }
        body.put("content", titleBuilder.toString());
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        expectFieldError(result, "content");

        //Document
        result.andDo(document("createTopic/tooLongContent"));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void create_validRequest_responseWithCreatedTopic() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Best features on the forum");
        body.put("content", "Hello, below you can write must usable features, let's go!");
        body.put("authorId", 1);

        //When
        ResultActions result = mockMvc.perform(
            post("/topic")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectTopic(result, 1);

        //Document
        result.andDo(document(
            "createTopic/success",
            requestFields(
                fieldWithPath("title").description("Title of created post. Should describe about what whole topic will be"),
                fieldWithPath("content").description("Content of the first post in this topic. Simple message that will be seen while viewing this post"),
                fieldWithPath("authorId").description("Author id. At the moment it's just an id of authenticated user")
            ),
            topicResponseFields(),
            topicLinks()
        ));
    }

    @Test
    @Sql({"valid-users-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getAll_noTopics_responseWithNoTopics() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectEmptyPage(result);
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getAll_threeTopics_responseWithSecondTopic() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic?size=2&page=1")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectTopicsPage(result, 4, 2);

        //Documentation
        result.andDo(document(
            "getAllTopics/success",
            requestParameters(
                pageParamDescriptor(),
                sizeParamDescriptor()
            ),
            topicsPageResponseFields(),
            topicsPageLinks()
        ));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void get_notExistingId_responseWithProperError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic/34")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Documentation
        result.andDo(document("getTopic/topicNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void get_existingId_responseWithTopic() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectTopic(result, 1);

        //Documentation
        result.andDo(document(
            "getTopic/success",
            pathParameters(
                parameterWithName("id").description("Id of topic you're looking for")
            ),
            topicResponseFields(),
            topicLinks()
        ));
    }


    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getPosts_topicNotFound_responseWithNoPosts() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic/34/post")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Documentation
        result.andDo(document("getTopicPosts/topicNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void getPosts_topicWithPosts_responseWithThesePosts() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            get("/topic/{id}/post?size=2&page=1", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectPostsPage(result, 3, 2);

        //Documentation
        result.andDo(document(
            "getTopicPosts/success",
            pathParameters(
                parameterWithName("id").description("Id of topic which posts you want to retrieve")
            ),
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
    @WithUserDetails("maria@gmail.com")
    public void close_invalidAuthorAuthenticated_responseWithProperError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/close", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.FORBIDDEN);

        //Document
        result.andDo(document("closeTopic/invalidAuthorAuthenticated"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void close_notExistingTopic_responseWithProperError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/close", 34)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("closeTopic/topicNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void close_validRequest_responseWithClosedTopic() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/close", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectTopic(result, 1);

        //Document
        result.andDo(document(
            "closeTopic/success",
            pathParameters(
                parameterWithName("id").description("Topic's id the user want to close")
            ),
            topicResponseFields(),
            topicLinks()
        ));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("maria@gmail.com")
    public void open_invalidAuthorAuthenticated_responseWithProperError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/open", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.FORBIDDEN);

        //Document
        result.andDo(document("openTopic/invalidAuthorAuthenticated"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void open_notExistingTopic_responseWithProperError() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/open", 34)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("openTopic/topicNotFound"));
    }

    @Test
    @Sql({"valid-users-data.sql", "valid-topics-data.sql", "valid-posts-data.sql"})
    @WithUserDetails("mark@gmail.com")
    public void open_validRequest_responseWithClosedTopic() throws Exception {
        //Given, when
        ResultActions result = mockMvc.perform(
            put("/topic/{id}/open", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result.andExpect(status().isOk());
        expectTopic(result, 1);

        //Document
        result.andDo(document(
            "openTopic/success",
            pathParameters(
                parameterWithName("id").description("Topic's id the user want to open")
            ),
            topicResponseFields(),
            topicLinks()
        ));
    }
}
