package pl.dkaluza.forum.core.validator;

import org.springframework.stereotype.Component;

/**
 * Class that eases validating many Validators using one method
 */
@Component
public class ComposedValidatorsExecutor {
    @SafeVarargs
    public final <T extends Exception> void validate(Validator<T>... validators) throws T {
        for (Validator<T> validator : validators) {
            validator.validate();
        }
    }
}
