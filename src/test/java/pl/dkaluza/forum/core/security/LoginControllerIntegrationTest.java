package pl.dkaluza.forum.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class LoginControllerIntegrationTest {
    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public LoginControllerIntegrationTest(ObjectMapper objectMapper) {
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
            ).build();
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "email*gmail.com", "not-existing-mail@gmail.com"})
    @Sql("valid-user-data.sql")
    public void login_invalidEmail_responseWithProperError(String email) throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", "v3ryh4rdp4ssw0rd");

        //When
        ResultActions result = mockMvc.perform(
            post("/login")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Documentation - is below
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "  sss ", "definitelyNotValidPassword"})
    @Sql("valid-user-data.sql")
    public void login_invalidPassword_responseWithProperError(String password) throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");
        body.put("password", password);

        //When
        ResultActions result = mockMvc.perform(
            post("/login")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Documentation
        result.andDo(document("security/login/badCredentials"));
    }

    @Test
    @Sql("disabled-user-data.sql")
    public void login_disabledUser_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");
        body.put("password", "v3ryh4rdp4ssw0rd");

        //When
        ResultActions result = mockMvc.perform(
            post("/login")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        //Documentation
        result.andDo(document("security/login/disabled"));
    }

    @Test
    @Sql("valid-user-data.sql")
    public void login_validData_responseWithJwtToken() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");
        body.put("password", "v3ryh4rdp4ssw0rd");

        //When
        ResultActions result = mockMvc.perform(
            post("/login")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.AUTHORIZATION));

        //Documentation
        result.andDo(document(
            "security/login/success",
            requestFields(
                fieldWithPath("email").description("Your account's e-mail address"),
                fieldWithPath("password").description("Password to the account")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("Generated JWT token")
            )
        ));
    }
}
