package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static pl.dkaluza.forum.utils.restdocs.HateoasUtils.*;

abstract class TopicSnippets {
    static ResponseFieldsSnippet topicResponseFields() {
        return responseFields(
            fieldWithPath("id").description("Topic's id"),
            fieldWithPath("title").description("Topic's title"),
            fieldWithPath("closed").description("Boolean which is set to true if topic is closed"),
            fieldWithPath("createdAt").description("Date-time when topic has been created"),
            fieldWithPath("authorId").description("User who created this topic"),
            subsectionWithPath("lastPost").description("Last topic's post"),
            linksFieldDescriptor()
        );
    }

    static LinksSnippet topicLinks() {
        return links(
            curiesLinkDescriptor(),
            selfLinkDescriptor(),
            linkWithRel("df:topicPosts").description("Link to posts that belong to the topic").attributes(docsAttribute("topicPosts.html")),
            linkWithRel("df:openTopic").description("Link used to open the topic").attributes(docsAttribute("openTopic.html")),
            linkWithRel("df:closeTopic").description("Link used to close the topic").attributes(docsAttribute("closeTopic.html"))
        );
    }

    static ResponseFieldsSnippet topicsPageResponseFields() {
        return responseFields(
            embeddedFieldDescriptor(),
            subsectionWithPath("_embedded.df:topic").description("List with topics on requested page. Default sorted descending by id"),
            linksFieldDescriptor(),
            pageFieldDescriptor()
        );
    }

    static LinksSnippet topicsPageLinks() {
        return links(
            curiesLinkDescriptor(),
            linkWithRel("df:createTopic").description("Link to create new topic").attributes(docsAttribute("createTopic.html")),
            linkWithRel("df:post").optional().description("Link to post with specified id").attributes(docsAttribute("post.html")),
            linkWithRel("df:topicPosts").optional().description("Link to posts that belong to the topic").attributes(docsAttribute("topicPosts.html")),
            linkWithRel("df:openTopic").optional().description("Link used to open the topic").attributes(docsAttribute("openTopic.html")),
            linkWithRel("df:closeTopic").optional().description("Link used to close the topic").attributes(docsAttribute("closeTopic.html")),
            firstPageLinkDescriptor(),
            prevPageLinkDescriptor(),
            selfPageLinkDescriptor(),
            nextPageLinkDescriptor(),
            lastPageLinkDescriptor()
        );
    }

    static ResponseFieldsSnippet postResponseFields() {
        return responseFields(
            fieldWithPath("id").description("Post's id"),
            fieldWithPath("content").description("Content of the post. Simple message that will be seen while viewing this post"),
            fieldWithPath("createdAt").description("Time when post has been created"),
            fieldWithPath("topicId").description("Id of the topic where post belongs"),
            fieldWithPath("authorId").description("User id who is author of the post"),
            linksFieldDescriptor()
        );
    }

    static LinksSnippet postLinks() {
        return links(
            selfLinkDescriptor(),
            curiesLinkDescriptor(),
            linkWithRel("df:topic").description("Link to topic where post belongs").attributes(docsAttribute("topic.html"))
        );
    }

    static ResponseFieldsSnippet postsPageResponseFields() {
        return responseFields(
            embeddedFieldDescriptor(),
            subsectionWithPath("_embedded.df:post").description("List with topics on requested page. Default sorted ascending by id"),
            linksFieldDescriptor(),
            pageFieldDescriptor()
        );
    }

    static LinksSnippet postsPageLinks() {
        return links(
            curiesLinkDescriptor(),
            linkWithRel("df:post").optional().description("Link to post with specified id").attributes(docsAttribute("post.html")),
            linkWithRel("df:topic").optional().description("Link to topic where post belongs to").attributes(docsAttribute("topic.html")),
            firstPageLinkDescriptor(),
            prevPageLinkDescriptor(),
            selfPageLinkDescriptor(),
            nextPageLinkDescriptor(),
            lastPageLinkDescriptor()
        );
    }

    static ResponseFieldsSnippet topicPostsPageResponseFields() {
        return postsPageResponseFields();
    }

    static LinksSnippet topicPostsPageLinks() {
        return links(
            curiesLinkDescriptor(),
            linkWithRel("df:createPost").optional().description("Link to create new post").attributes(docsAttribute("createPost.html")),
            linkWithRel("df:post").optional().description("Link to post with specified id").attributes(docsAttribute("post.html")),
            linkWithRel("df:topic").optional().description("Link to topic where post belongs to").attributes(docsAttribute("topic.html")),
            firstPageLinkDescriptor(),
            prevPageLinkDescriptor(),
            selfPageLinkDescriptor(),
            nextPageLinkDescriptor(),
            lastPageLinkDescriptor()
        );
    }
}
