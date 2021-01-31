package pl.dkaluza.forum.core.mappers;

public interface ObjectMapper<O, M> {
    O toObject(M model);
}
