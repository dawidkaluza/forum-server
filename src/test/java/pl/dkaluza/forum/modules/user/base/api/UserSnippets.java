package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static pl.dkaluza.forum.utils.restdocs.HateoasUtils.*;

abstract class UserSnippets {
    static ResponseFieldsSnippet userResponseFields() {
        return responseFields(
            fieldWithPath("id").description("User's id"),
            fieldWithPath("name").description("User's name. Used in general by other users to recognize who is who"),
            fieldWithPath("email").description("User's email. Mostly used by the server in processes like confirming registration, resetting password, etc."),
            fieldWithPath("enabled").description("Boolean that is set to true whether the user is enabled (in general via e-mail confirmation)"),
            linksFieldDescriptor()
        );
    }

    static LinksSnippet userLinks() {
        return links(
            selfLinkDescriptor()
        );
    }

    static ResponseFieldsSnippet registeredUserResponseFields() {
        return userResponseFields();
    }

    static LinksSnippet registeredUserLinks() {
        return links(
            curiesLinkDescriptor(),
            selfLinkDescriptor(),
            linkWithRel("df:confirmRegistration").description("Link where user can confirm the registration").attributes(docsAttribute("confirmRegistration.html")),
            linkWithRel("df:resendRegistrationToken").description("Link where user can resend the token to its e-mail address").attributes(docsAttribute("resendRegistrationToken.html"))
        );
    }

    static ResponseFieldsSnippet usersPageResponseFields() {
        return responseFields(
            embeddedFieldDescriptor(),
            subsectionWithPath("_embedded.df:user").description("List to get the users on the requested page"),
            linksFieldDescriptor(),
            pageFieldDescriptor()
        );
    }

    static LinksSnippet usersPageLinks() {
        return links(
            curiesLinkDescriptor(),
            linkWithRel("df:user").optional().description("Link to the user with given id").attributes(docsAttribute("user.html")),
            firstPageLinkDescriptor(),
            prevPageLinkDescriptor(),
            selfPageLinkDescriptor(),
            nextPageLinkDescriptor(),
            lastPageLinkDescriptor()
        );
    }
}
