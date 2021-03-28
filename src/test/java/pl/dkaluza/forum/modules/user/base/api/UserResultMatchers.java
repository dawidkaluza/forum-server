package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

abstract class UserResultMatchers {
    static void expectUser(ResultActions result, long id) throws Exception {
        result
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.email").exists())
            .andExpect(jsonPath("$.enabled").exists())
            .andExpect(jsonPath("$._links.self").exists());
    }

    static void expectUsersPage(ResultActions result, long expectedFirstId, int expectedListLength) throws Exception {
        result
            .andExpect(jsonPath("$._embedded.df:user").isArray())
            .andExpect(jsonPath("$._embedded.df:user.length()").value(expectedListLength))
            .andExpect(jsonPath("$._embedded.df:user[0].id").value(expectedFirstId))
            .andExpect(jsonPath("$._embedded.df:user[0].name").exists())
            .andExpect(jsonPath("$._embedded.df:user[0].email").exists())
            .andExpect(jsonPath("$._embedded.df:user[0].enabled").exists())
            .andExpect(jsonPath("$._embedded.df:user[0]._links").exists())
            .andExpect(jsonPath("$._embedded.df:user[0]._links.self").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").exists())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page").exists());
    }
}
