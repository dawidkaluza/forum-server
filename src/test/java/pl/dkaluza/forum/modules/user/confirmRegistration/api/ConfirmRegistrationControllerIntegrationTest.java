package pl.dkaluza.forum.modules.user.confirmRegistration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.dkaluza.forum.core.restdocs.LinksUtils;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.core.restdocs.LinksUtils.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class ConfirmRegistrationControllerIntegrationTest {
    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public ConfirmRegistrationControllerIntegrationTest(ObjectMapper objectMapper, UserRepository userRepository) {
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(
                documentationConfiguration(restDoc)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build();
    }

    @Test
    @Sql("valid-data.sql")
    public void confirmRegistration_tokenNotFound_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("id", 69L);
        body.put("token", "123xyz");

        //When
        ResultActions result = mockMvc.perform(
            put("/confirmRegistration/confirm")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Document
        //Dont need to document, response is the same as in 'invalidToken' case
    }

    @Test
    @Sql("valid-data.sql")
    public void confirmRegistration_invalidToken_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1L);
        body.put("token", "itsNotValidToken");

        //When
        ResultActions result = mockMvc.perform(
            put("/confirmRegistration/confirm")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Document
        result.andDo(document("user/confirmRegistration/invalidToken"));
    }

    @Test
    @Sql("token-expired-data.sql")
    public void confirmRegistration_tokenExpired_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1L);
        body.put("token", "123xyz");

        //When
        ResultActions result = mockMvc.perform(
            put("/confirmRegistration/confirm")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isGone())
            .andExpect(jsonPath("$.status").value(410))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Document
        result.andDo(document("user/confirmRegistration/tokenExpired"));
    }

    @Test
    @Sql("valid-data.sql")
    public void confirmRegistration_validData_responseWithLinks() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1L);
        body.put("token", "123xyz");

        //When
        ResultActions result = mockMvc.perform(
            put("/confirmRegistration/confirm")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk());

        //Document
        result.andDo(document(
            "user/confirmRegistration/success",
            requestFields(
                fieldWithPath("id").description("User id to confirm registration"),
                fieldWithPath("token").description("Token that he received on e-mail")
            ),
            responseFields(
                linksFieldDescriptor()
            ),
            links(
                curiesLinkDescriptor()
            )
        ));
    }

    @Test
    @Sql("valid-data.sql")
    public void resendToken_userNotFound_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "kram@gmail.com");

        //When
        ResultActions result = mockMvc.perform(
            post("/confirmRegistration/resendToken")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @Sql("valid-data.sql")
    public void resendToken_tokenNotFound_responseWithProperError() throws Exception {

    }

    @Test
    @Sql("too-many-tries-data.sql")
    public void resendToken_tooManyTries_responseWithProperError() throws Exception {

    }

    @Test
    @Sql("valid-data.sql")
    public void resendToken_validData_emptyResponse() throws Exception {

    }
}