package pl.dkaluza.forum.core.api.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Component
public class LocaleFieldErrorMapper {
    private final MessageSource messageSource;

    @Autowired
    public LocaleFieldErrorMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public LocaleFieldError map(String field, String code, Locale locale) {
        return new LocaleFieldError(
            field,
            messageSource.getMessage(code, null, locale)
        );
    }

    public LocaleFieldError map(FieldError error, Locale locale) {
        return new LocaleFieldError(
            error.getField(),
            messageSource.getMessage(error, locale)
        );
    }

    public List<LocaleFieldError> map(Collection<? extends FieldError> errors, Locale locale) {
        List<LocaleFieldError> localeErrors = new ArrayList<>();
        for (FieldError error : errors) {
            localeErrors.add(
                map(error, locale)
            );
        }
        return localeErrors;
    }
}
