package pl.dkaluza.forum.core.mappers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface ModelMapper<T, U extends RepresentationModel<?>> extends RepresentationModelAssembler<T, U> {
    @Override
    U toModel(T object);
}
