package pl.dkaluza.forum.modules.index.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.core.restdocs.LinksUtils.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class IndexControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(documentationConfiguration(restDoc))
            .build();
    }

    @Test
    public void index_requestGet_returnIndexModel() throws Exception {
        //Given, When
        ResultActions result = mockMvc.perform(
            get("/")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.curies").exists())
            .andExpect(jsonPath("_links.df:register").exists())
            .andExpect(jsonPath("message").exists());

        //Document
        result
            .andDo(document(
                "index",
                links(
                    selfLinkDescriptor(),
                    curiesLinkDescriptor(),
                    linkWithRel("df:register").description("Register new account")
                ),
                responseFields(
                    fieldWithPath("message").description("Just a welcome message :)"),
                    linksFieldDescriptor()
                )
            ));


    }
}
