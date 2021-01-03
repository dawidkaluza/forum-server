package pl.dkaluza.forum.core.validator.apiRequest;

import pl.dkaluza.forum.core.exceptions.ApiRequestException;
import pl.dkaluza.forum.core.validator.Validator;

public interface ApiRequestValidator extends Validator<ApiRequestException> {
    @Override
    void validate() throws ApiRequestException;
}
