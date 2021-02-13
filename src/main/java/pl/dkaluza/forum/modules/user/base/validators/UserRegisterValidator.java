package pl.dkaluza.forum.modules.user.base.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserRegisterValidator implements Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\S+@\\S+\\.\\S+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[^\\S]{3,32}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[^\\S]{5,32}$");
    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> objectClass) {
        return objectClass.equals(UserRegisterModel.class);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plainPassword", "empty");

        UserRegisterModel model = (UserRegisterModel) object;

        String email = model.getEmail();
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            errors.rejectValue("email", "invalid");
        }

        String name = model.getName();
        Matcher nameMatcher = NAME_PATTERN.matcher(name);
        if (!nameMatcher.matches()) {
            errors.rejectValue("name", "invalid");
        }

        String password = model.getPlainPassword();
        Matcher passwordMatcher = PASSWORD_PATTERN.matcher(password);
        if (!passwordMatcher.matches()) {
            errors.rejectValue("plainPassword", "invalid");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            errors.rejectValue("email", "exists");
        }

        if (userRepository.findByName(name).isPresent()) {
            errors.rejectValue("name", "exists");
        }
    }
}
