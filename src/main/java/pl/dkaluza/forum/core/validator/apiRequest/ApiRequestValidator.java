package pl.dkaluza.forum.core.validator.apiRequest;

import pl.dkaluza.forum.core.validator.Validator;

public interface ApiRequestValidator extends Validator<RuntimeException> {
    @Override
    void validate() throws RuntimeException;
}
