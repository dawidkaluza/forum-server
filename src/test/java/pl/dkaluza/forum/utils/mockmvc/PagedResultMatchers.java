package pl.dkaluza.forum.utils.mockmvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public abstract class PagedResultMatchers {
    //TODO add checking there is not _embedded field?
    public static void expectEmptyPage(ResultActions result) throws Exception {
        result
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$.page").exists());
    }
}
