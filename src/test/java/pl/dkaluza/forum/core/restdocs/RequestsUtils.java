package pl.dkaluza.forum.core.restdocs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class RequestsUtils {
    public static ParameterDescriptor pageParamDescriptor() {
        return parameterWithName("page").optional().description("Specifies which page with results you want to get. Remember: first page is 0. Default value is 0");
    }

    public static ParameterDescriptor sizeParamDescriptor() {
        return parameterWithName("size").optional().description("Specifies how many results you will see per page. Default value is 10");
    }

    public static FieldDescriptor pageFieldDescriptor() {
        return subsectionWithPath("page").description("Pagination data which shows current page size, page number, total elements and total pages for the request");
    }
}
