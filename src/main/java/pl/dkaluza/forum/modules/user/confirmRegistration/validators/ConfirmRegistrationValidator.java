package pl.dkaluza.forum.modules.user.confirmRegistration.validators;

import pl.dkaluza.forum.core.validator.Validator;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.InvalidTokenException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenExpiredException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;

public class ConfirmRegistrationValidator implements Validator<RuntimeException> {
    private final Long id;
    private final String tokenCode;
    private final ConfirmRegistrationTokenRepository repository;

    private ConfirmRegistrationToken token;

    public ConfirmRegistrationValidator(Long id, String tokenCode, ConfirmRegistrationTokenRepository repository) {
        this.id = id;
        this.tokenCode = tokenCode;
        this.repository = repository;
    }

    @Override
    public void validate() throws RuntimeException {
        token = repository
            .findById(id)
            .orElseThrow(() -> new TokenNotFoundException(id));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (!token.getToken().equals(tokenCode)) {
            throw new InvalidTokenException();
        }
    }

    public ConfirmRegistrationToken getToken() {
        return token;
    }
}
