package pl.dkaluza.forum.modules.user.confirmRegistration.api;

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

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectError;
import static pl.dkaluza.forum.utils.restdocs.HateoasUtils.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith({SpringExtension.class})
public class ConfirmRegistrationControllerTest {
    @RegisterExtension
    final RestDocumentationExtension restDoc = new RestDocumentationExtension("target/generated-snippets/user/confirmRegistration");

    private final ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public ConfirmRegistrationControllerTest(ObjectMapper objectMapper) {
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

    @ParameterizedTest
    @ValueSource(strings = { "1|itsNotValidToken", "69|123xyz"})
    @Sql("valid-data.sql")
    public void confirmRegistration_tokenNotFound_responseWithProperError(String input) throws Exception {
        //Given
        String[] inputs = input.split("\\|");
        Map<String, Object> body = new HashMap<>();
        body.put("id", Long.valueOf(inputs[0]));
        body.put("token", inputs[1]);

        //When
        ResultActions result = mockMvc.perform(
            put("/confirmRegistration")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("confirmRegistration/tokenNotFound"));
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
            put("/confirmRegistration")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.GONE);

        //Document
        result.andDo(document("confirmRegistration/tokenExpired"));
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
            put("/confirmRegistration")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._links.df:login").exists());

        //Document
        result.andDo(document(
            "confirmRegistration/success",
            requestFields(
                fieldWithPath("id").description("User's id who want to confirm the registration"),
                fieldWithPath("token").description("Token sent in e-mail message to the user")
            ),
            responseFields(
                linksFieldDescriptor()
            ),
            links(
                curiesLinkDescriptor(),
                linkWithRel("df:login").description("Logins into account").attributes(docsAttribute("login.html"))
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
        expectError(result, HttpStatus.NOT_FOUND);


        //Document
        result.andDo(document("resendToken/userNotFound"));
    }

    @Test
    @Sql("no-token-data.sql")
    public void resendToken_tokenNotFound_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");

        //When
        ResultActions result = mockMvc.perform(
            post("/confirmRegistration/resendToken")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.NOT_FOUND);

        //Document
        result.andDo(document("resendToken/tokenNotFound"));
    }

    @Test
    @Sql("too-many-tries-data.sql")
    public void resendToken_tooManyTries_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");

        //When
        ResultActions result = mockMvc.perform(
            post("/confirmRegistration/resendToken")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.LOCKED);

        //Document
        result.andDo(document("resendToken/tooManyTries"));
    }

    @Test
    @Sql("valid-data.sql")
    public void resendToken_validData_responseWithConfirmationLink() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "mark@gmail.com");

        //When
        ResultActions result = mockMvc.perform(
            post("/confirmRegistration/resendToken")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$._links.df:confirmRegistration").exists())
        ;

        //Document
        result.andDo(document(
            "resendToken/success",
            requestFields(
                fieldWithPath("email").description("User's email who needs to resend the token")
            ),
            responseFields(
                linksFieldDescriptor()
            ),
            links(
                curiesLinkDescriptor(),
                linkWithRel("df:confirmRegistration").description("Link where the user can confirm its registration").attributes(docsAttribute("confirmRegistration.html"))
            )
        ));
    }
}
