package pl.dkaluza.forum.core.mappers;

import org.springframework.hateoas.RepresentationModel;

public interface ModelAndObjectMapper<T, U extends RepresentationModel<?>> extends ModelMapper<T, U>, ObjectMapper<T, U> {
}
