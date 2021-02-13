package pl.dkaluza.forum.modules.user.base.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.InvalidPasswordException;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegisterValidatorTest {
    @Test
    public void validate_emailAlreadyExists_throwEmailAlreadyExistsException() {
        //Given
        UserRegisterModel model = getUserRegisterModel("Hector", "existing@mail.com", "iziPassword");
        UserRepository repository = getUserRepository();
        UserRegisterValidator validator = new UserRegisterValidator(model, repository);

        //When, then
        assertThrows(EmailAlreadyExistsException.class, validator::validate);
    }

    @Test
    public void validate_notEmail_throwInvalidEmailException() {
        //TODO
    }

    @Test
    public void validate_tooShortEmail_throwInvalidEmailException() {
        //TODO
    }

    @Test
    public void validate_tooLongEmail_throwInvalidEmailException() {
        //TODO
    }

    @Test
    public void validate_nameAlreadyExists_throwNameAlreadyExistsException() {
        //Given
        UserRegisterModel model = getUserRegisterModel("existingName", "heccy@o2.pl", "iziPassword");
        UserRepository repository = getUserRepository();
        UserRegisterValidator validator = new UserRegisterValidator(model, repository);

        //When, then
        assertThrows(EmailAlreadyExistsException.class, validator::validate);
    }

    @Test
    public void validate_tooShortName_throwInvalidNameException() {
        //TODO
    }

    @Test
    public void validate_tooLongName_throwInvalidNameException() {
        //TODO
    }

    @ParameterizedTest
    @ValueSource(strings = { "1", "12", "12 ", " 12", " ab  "})
    public void validate_tooShortPassword_throwInvalidPasswordException(String shortPassword) {
        //Given
        UserRegisterModel model = getUserRegisterModel("Heccy", "heccy@o2.pl", shortPassword);
        UserRepository repository = getUserRepository();
        UserRegisterValidator validator = new UserRegisterValidator(model, repository);

        //When, then
        assertThrows(InvalidPasswordException.class, validator::validate);
    }

    @Test
    public void validate_tooLongPassword_throwException() {

    }

    @Test
    public void validate_fullyCorrectModel_noException() {
    }

    private static UserRegisterModel getUserRegisterModel(String name, String email, String plainPassword) {
        UserRegisterModel model = new UserRegisterModel();
        model.setName(name);
        model.setEmail(email);
        model.setPlainPassword(plainPassword);
        return model;
    }

    private static UserRepository getUserRepository() {
        UserRepository repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenAnswer(inv -> {
            if (inv.getArgument(0).equals("existing@mail.com")) {
                return Optional.of(new User());
            }

            return Optional.empty();
        });
        Mockito.when(repository.findByName(Mockito.anyString())).thenAnswer(inv -> {
            if (inv.getArgument(0).equals("existingName")) {
                return Optional.of(new User());
            }

            return Optional.empty();
        });
        return repository;
    }
}
