package pl.dkaluza.forum.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectError;
import static pl.dkaluza.forum.utils.mockmvc.ErrorResultMatchers.expectFieldError;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class ErrorControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(documentationConfiguration(restDoc))
            .build();
    }

    @Test
    public void error_validRequest_validError() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/error/example")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        expectError(result, HttpStatus.I_AM_A_TEAPOT);
        expectFieldError(result, "some.field");

        //Documentation
        result.andDo(document("error",
            responseFields(
                fieldWithPath("status").description("Http status"),
                fieldWithPath("message").description("Message that describes what happened in general"),
                fieldWithPath("timestamp").description("Timestamp when error occurs (format: dd-MM-yyyy HH:mm:ss)"),
                fieldWithPath("fieldErrors").optional().description("Optional array with field errors"),
                fieldWithPath("fieldErrors[].field").description("Field's path where error occurred"),
                fieldWithPath("fieldErrors[].message").description("Description of the error")
            )
        ));
    }
}
