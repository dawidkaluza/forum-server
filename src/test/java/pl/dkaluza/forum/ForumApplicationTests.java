package pl.dkaluza.forum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class ForumApplicationTests {
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach(WebApplicationContext webAppContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document(
                "{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())
            ))
            .build();
    }

    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isNoContent())
            .andDo(document("index"));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        mockMvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andDo(document("users"));
    }

    /*

	@Test
	public void indexExample() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andDo(document("index-example",
					links(
							linkWithRel("notes").description("The <<resources_notes,Notes resource>>"),
							linkWithRel("tags").description("The <<resources_tags,Tags resource>>"),
							linkWithRel("profile").description("The ALPS profile for the service")),
					responseFields(
							subsectionWithPath("_links").description("<<resources_index_access_links,Links>> to other resources"))));

	}
     */

}
