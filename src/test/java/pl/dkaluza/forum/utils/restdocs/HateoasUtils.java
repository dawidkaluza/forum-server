package pl.dkaluza.forum.utils.restdocs;

import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class HateoasUtils {
    public static FieldDescriptor linksFieldDescriptor(String path) {
        return subsectionWithPath(path).description("Links to resources");
    }

    public static FieldDescriptor linksFieldDescriptor() {
        return linksFieldDescriptor("_links");
    }

    public static FieldDescriptor embeddedFieldDescriptor() {
        return fieldWithPath("_embedded").description("Child resources, where keys are relations to them");
    }

    public static FieldDescriptor pageFieldDescriptor() {
        return subsectionWithPath("page").description("Pagination data which shows information like: current page size, page number, total elements and total pages for the request");
    }

    public static LinkDescriptor selfLinkDescriptor() {
        return linkWithRel("self").description("Link to itself. Sometimes provides more details than current representation");
    }

    public static LinkDescriptor curiesLinkDescriptor() {
        return linkWithRel("curies").description("Curies that describe custom HAL relations");
    }

    public static LinkDescriptor firstPageLinkDescriptor() {
        return linkWithRel("first").optional().description("Link to the first page");
    }

    public static LinkDescriptor prevPageLinkDescriptor() {
        return linkWithRel("prev").optional().description("Link to the previous page");
    }

    public static LinkDescriptor selfPageLinkDescriptor() {
        return linkWithRel("self").description("Link to the current page");
    }

    public static LinkDescriptor nextPageLinkDescriptor() {
        return linkWithRel("next").optional().description("Link to the next page");
    }

    public static LinkDescriptor lastPageLinkDescriptor() {
        return linkWithRel("last").optional().description("Link to the last page");
    }

    public static Attributes.Attribute docsAttribute(String uri) {
        return new Attributes.Attribute("docs", uri);
    }
}
