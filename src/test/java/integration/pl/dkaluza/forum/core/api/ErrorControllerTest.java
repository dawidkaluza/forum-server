package pl.dkaluza.forum.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
            get("/error")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.fieldErrors[*].object").exists())
            .andExpect(jsonPath("$.fieldErrors[*].field").exists())
            .andExpect(jsonPath("$.fieldErrors[*].message").exists());

        //Documentation
        result.andDo(document("error",
            responseFields(
                fieldWithPath("status").description("Http status"),
                fieldWithPath("message").description("Message that describes what happened"),
                fieldWithPath("timestamp").description("Timestamp when error occurs (format: dd-MM-yyyy HH:mm:ss)"),
                fieldWithPath("fieldErrors").optional().description("Optional array with field errors"),
                fieldWithPath("fieldErrors[].object").description("Object name of this field error"),
                fieldWithPath("fieldErrors[].field").description("Field name of this field error"),
                fieldWithPath("fieldErrors[].message").description("Message of this field error")
            )
        ));
    }
}
