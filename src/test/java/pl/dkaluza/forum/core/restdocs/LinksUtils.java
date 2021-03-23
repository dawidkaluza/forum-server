package pl.dkaluza.forum.core.restdocs;

import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class LinksUtils {
    public static FieldDescriptor embeddedFieldDescriptor() {
        return fieldWithPath("_embedded").description("Child resources, where keys are relations to them");
    }

    public static FieldDescriptor linksFieldDescriptor(String path) {
        return subsectionWithPath(path).description("Links to resources");
    }

    public static FieldDescriptor linksFieldDescriptor() {
        return linksFieldDescriptor("_links");
    }

    public static LinkDescriptor selfLinkDescriptor() {
        return linkWithRel("self").description("Link to itself");
    }

    public static LinkDescriptor curiesLinkDescriptor() {
        return linkWithRel("curies").description("Curies that describe custom HAL relations");
    }

    public static LinkDescriptor firstPageLinkDescriptor() {
        return linkWithRel("first").optional().description("Link to first page");
    }

    public static LinkDescriptor prevPageLinkDescriptor() {
        return linkWithRel("prev").optional().description("Link to previous page");
    }

    public static LinkDescriptor selfPageLinkDescriptor() {
        return linkWithRel("self").description("Link to current page");
    }

    public static LinkDescriptor nextPageLinkDescriptor() {
        return linkWithRel("next").optional().description("Link to next page");
    }

    public static LinkDescriptor lastPageLinkDescriptor() {
        return linkWithRel("last").optional().description("Link to last page");
    }

    public static Attributes.Attribute docsAttribute(String uri) {
        return new Attributes.Attribute("docs", uri);
    }
}
