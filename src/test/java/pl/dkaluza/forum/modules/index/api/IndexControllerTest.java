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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dkaluza.forum.utils.restdocs.HateoasUtils.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class IndexControllerTest {
    private MockMvc mockMvc;

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
            .andExpect(jsonPath("_links.df:login").exists())
            .andExpect(jsonPath("_links.df:register").exists())
            .andExpect(jsonPath("_links.df:users").exists())
            .andExpect(jsonPath("_links.df:topics").exists())
            .andExpect(jsonPath("_links.df:posts").exists())
            .andExpect(jsonPath("message").exists());

        //Document
        result.andDo(document(
            "index/success",
            links(
                selfLinkDescriptor(),
                curiesLinkDescriptor(),
                linkWithRel("df:login").description("Logins into existing user account").attributes(docsAttribute("login.html")),
                linkWithRel("df:register").description("Registers new user account").attributes(docsAttribute("register.html")),
                linkWithRel("df:users").description("Lists all registered users").attributes(docsAttribute("users.html")),
                linkWithRel("df:topics").description("Lists all topics in order from newest to oldest").attributes(docsAttribute("topics.html")),
                linkWithRel("df:posts").description("Lists all posts in order from newest to oldest").attributes(docsAttribute("posts.html"))
            ),
            responseFields(
                fieldWithPath("message").description("Just a welcome message :)"),
                linksFieldDescriptor()
            )
        ));
    }
}
