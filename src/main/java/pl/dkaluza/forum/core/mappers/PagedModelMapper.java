package pl.dkaluza.forum.core.mappers;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.stream.Collectors;

public abstract class PagedModelMapper<T, U extends RepresentationModel<?>> implements ModelMapper<Page<T>, PagedModel<U>> {
    private final ModelAndObjectMapper<T, U> mapper;

    public PagedModelMapper(ModelAndObjectMapper<T, U> mapper) {
        this.mapper = mapper;
    }

    protected abstract Iterable<Link> getLinks();

    @Override
    public PagedModel<U> toModel(Page<T> page) {
        return PagedModel.of(
            page
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList()),
            new PagedModel.PageMetadata(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
            ),
            getLinks()
        );
    }
}
