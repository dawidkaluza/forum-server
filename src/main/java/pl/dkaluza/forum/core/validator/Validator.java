package pl.dkaluza.forum.core.validator;

public interface Validator<T extends Exception> {
    void validate() throws T;
}
