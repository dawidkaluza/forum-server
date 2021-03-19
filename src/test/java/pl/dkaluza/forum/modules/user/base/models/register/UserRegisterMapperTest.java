package pl.dkaluza.forum.modules.user.base.models.register;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegisterMapperTest {
    private static PasswordEncoder passwordEncoder;
    private UserRegisterMapper userRegisterMapper;

    @BeforeAll
    public static void beforeAll() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @BeforeEach
    public void beforeEach() {
        userRegisterMapper = new UserRegisterMapper(passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("provideModelAndExpectedUser")
    public void toObject_providedModel_returnExpectedObject(UserRegisterModel providedModel, User expectedUser) {
        //When
        User user = userRegisterMapper.toObject(providedModel);

        //Then
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(
            providedModel.getPlainPassword(), expectedUser.getEncodedPassword()
        ));
        assertEquals(expectedUser.isEnabled(), user.isEnabled());
    }

    private static Stream<Arguments> provideModelAndExpectedUser() {
        return Stream.of(
            Arguments.of(
                getUserRegisterModel("Mark", "markmail@gmail.com", "someHardPassword"),
                getUser("Mark", "markmail@gmail.com", passwordEncoder.encode("someHardPassword"))
            ),
            Arguments.of(
                getUserRegisterModel("Helliiccoopptteerr", "heillaaa@gmail.com", "12345671234567123456712345671234567"),
                getUser("Helliiccoopptteerr", "heillaaa@gmail.com", passwordEncoder.encode("12345671234567123456712345671234567"))
            ),
            Arguments.of(
                getUserRegisterModel("Name name name", "null", "&*^!@*&(#@!(*#*&)!@#"),
                getUser("Name name name", "null", passwordEncoder.encode("&*^!@*&(#@!(*#*&)!@#"))
            )
        );
    }

    private static UserRegisterModel getUserRegisterModel(String name, String email, String plainPassword) {
        UserRegisterModel model = new UserRegisterModel();
        model.setName(name);
        model.setEmail(email);
        model.setPlainPassword(plainPassword);
        return model;
    }


    private static User getUser(String name, String email, String encodedPassword) {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(null);
        Mockito.when(user.getName()).thenReturn(name);
        Mockito.when(user.getEmail()).thenReturn(email);
        Mockito.when(user.getEncodedPassword()).thenReturn(encodedPassword);
        Mockito.when(user.isEnabled()).thenReturn(false);
        return user;
    }
}
