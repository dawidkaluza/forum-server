package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

abstract class TopicResultMatchers {
    static void expectTopic(ResultActions result, long id) throws Exception {
        result
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.title").exists())
            .andExpect(jsonPath("$.closed").exists())
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.authorId").exists())
            .andExpect(jsonPath("$.lastPost").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.df:topicPosts").exists())
            .andExpect(jsonPath("$._links.df:openTopic").exists())
            .andExpect(jsonPath("$._links.df:closeTopic").exists())
            .andExpect(jsonPath("$._links.curies").exists());
    }

    static void expectTopicsPage(ResultActions result, long expectedFirstId, int expectedListLength) throws Exception {
        result
            .andExpect(jsonPath("$._embedded.df:topic").exists())
            .andExpect(jsonPath("$._embedded.df:topic.length()").value(expectedListLength))
            .andExpect(jsonPath("$._embedded.df:topic[0].id").value(expectedFirstId))
            .andExpect(jsonPath("$._embedded.df:topic[0].title").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0].closed").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0].createdAt").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0].authorId").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0].lastPost").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0]._links.self").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0]._links.df:topicPosts").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0]._links.df:openTopic").exists())
            .andExpect(jsonPath("$._embedded.df:topic[0]._links.df:closeTopic").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").exists())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page").exists());
    }

    static void expectPost(ResultActions result, long id) throws Exception {
        result
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.topicId").exists())
            .andExpect(jsonPath("$.authorId").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.df:topic").exists());
    }

    static void expectPostsPage(ResultActions result, long expectedFirstId, int expectedListLength) throws Exception {
        result
            .andExpect(jsonPath("$._embedded.df:post").exists())
            .andExpect(jsonPath("$._embedded.df:post.length()").value(expectedListLength))
            .andExpect(jsonPath("$._embedded.df:post[0].id").value(expectedFirstId))
            .andExpect(jsonPath("$._embedded.df:post[0].content").exists())
            .andExpect(jsonPath("$._embedded.df:post[0].createdAt").exists())
            .andExpect(jsonPath("$._embedded.df:post[0].topicId").exists())
            .andExpect(jsonPath("$._embedded.df:post[0].authorId").exists())
            .andExpect(jsonPath("$._embedded.df:post[*]._links.self").exists())
            .andExpect(jsonPath("$._embedded.df:post[*]._links.df:topic").exists())
            .andExpect(jsonPath("$._links.first").exists())
            .andExpect(jsonPath("$._links.prev").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.next").exists())
            .andExpect(jsonPath("$._links.last").exists())
            .andExpect(jsonPath("$._links.curies").exists())
            .andExpect(jsonPath("$.page").exists());
    }
}
