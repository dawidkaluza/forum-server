package pl.dkaluza.forum.core.mappers;

public interface ModelMapper<O, M> {
    M toModel(O object);
}
