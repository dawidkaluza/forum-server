package pl.dkaluza.forum.modules.user.confirmRegistration.validators;

import pl.dkaluza.forum.core.validator.Validator;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.InvalidTokenException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenExpiredException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;

public class ConfirmRegistrationValidator implements Validator<RuntimeException> {
    private final ConfirmModel model;
    private final ConfirmRegistrationTokenRepository repository;

    private ConfirmRegistrationToken token;

    public ConfirmRegistrationValidator(ConfirmModel model, ConfirmRegistrationTokenRepository repository) {
        this.model = model;
        this.repository = repository;
    }

    @Override
    public void validate() throws RuntimeException {
        long id = model.getId();
        token = repository
            .findById(id)
            .orElseThrow(() -> new TokenNotFoundException(id));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (!token.getToken().equals(model.getToken())) {
            throw new InvalidTokenException();
        }
    }

    public ConfirmRegistrationToken getToken() {
        return token;
    }
}
