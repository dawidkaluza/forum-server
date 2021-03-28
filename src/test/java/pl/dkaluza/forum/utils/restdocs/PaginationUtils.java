package pl.dkaluza.forum.utils.restdocs;

import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class PaginationUtils {
    public static ParameterDescriptor pageParamDescriptor() {
        return parameterWithName("page").optional().description("Specifies which page with results you want to get. Pages are counted from 0, so to get first page you'd like to set page=0. Default value is 0");
    }

    public static ParameterDescriptor sizeParamDescriptor() {
        return parameterWithName("size").optional().description("Specifies how many results you will see per page. Default value is 10");
    }

}
