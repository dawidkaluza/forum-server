package pl.dkaluza.forum.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ExternalEmailValidator implements ConstraintValidator<ExternalEmail, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    @Override
    public void initialize(ExternalEmail annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return EMAIL_PATTERN.matcher(value).matches();
    }
}

