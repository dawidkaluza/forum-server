package pl.dkaluza.forum.core.mappers;

import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;

public interface ObjectMapper<T, U> {
    T toObject(U model) throws EntityNotFoundException;
}
