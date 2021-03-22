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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class ErrorControllerIntegrationTest {
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
            get("/error")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isIAmATeapot())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.I_AM_A_TEAPOT.value()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.fieldErrors[*].field").exists())
            .andExpect(jsonPath("$.fieldErrors[*].message").exists());

        //Documentation
        result.andDo(document("error",
            responseFields(
                fieldWithPath("status").description("Http status"),
                fieldWithPath("message").description("Message that describes what happened"),
                fieldWithPath("timestamp").description("Timestamp when error occurs (format: dd-MM-yyyy HH:mm:ss)"),
                fieldWithPath("fieldErrors").optional().description("Optional array with field errors"),
                fieldWithPath("fieldErrors[].field").description("Field name of this field error"),
                fieldWithPath("fieldErrors[].message").description("Message of this field error")
            )
        ));
    }
}
