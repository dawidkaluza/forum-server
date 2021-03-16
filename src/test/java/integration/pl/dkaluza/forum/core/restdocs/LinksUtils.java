package pl.dkaluza.forum.core.restdocs;

import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class LinksUtils {
    public static FieldDescriptor linksFieldDescriptor() {
        return subsectionWithPath("_links").description("Links to resources");
    }

    public static LinkDescriptor selfLinkDescriptor() {
        return linkWithRel("self").description("Link to itself");
    }

    public static LinkDescriptor curiesLinkDescriptor() {
        return linkWithRel("curies").description("Curies that describe custom HAL relations");
    }

    public static LinkDescriptor firstLinkDescriptor() {
        return linkWithRel("first").description("First element of the whole collection");
    }

    public static LinkDescriptor lastLinkDescriptor() {
        return linkWithRel("last").description("Last element of the whole collection");
    }

    public static LinkDescriptor prevLinkDescriptor() {
        return linkWithRel("prev").description("Previous page of the collection");
    }

    public static LinkDescriptor nextLinkDescriptor() {
        return linkWithRel("next").description("Next page of the collection");
    }
}
