package pl.dkaluza.forum.modules.user.base.models.basic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    public void beforeEach() {
        userMapper = new UserMapper();
    }

    @ParameterizedTest
    @MethodSource("provideUserAndExpectedModel")
    public void toModel_providedUser_returnExpectedModel(User providedUser, UserModel expectedModel) {
        //When
        UserModel model = userMapper.toModel(providedUser);

        //Then
        assertEquals(expectedModel.getId(), model.getId());
        assertEquals(expectedModel.getName(), model.getName());
        assertEquals(expectedModel.getEmail(), model.getEmail());
        assertEquals(expectedModel.isEnabled(), model.isEnabled());
    }

    private static Stream<Arguments> provideUserAndExpectedModel() {
        return Stream.of(
            Arguments.of(
                getUser(1L, "Mark", "markomarkomarkomarkomarko@gmail.com", true),
                getUserModel(1L, "Mark", "markomarkomarkomarkomarko@gmail.com", true)
            ),
            Arguments.of(
                getUser(Long.MIN_VALUE, "", "1234567", false),
                getUserModel(Long.MIN_VALUE, "", "1234567", false)
            ),
            Arguments.of(
                getUser(Long.MAX_VALUE, "Helliiccoopptteerr", "1234567", false),
                getUserModel(Long.MAX_VALUE, "Helliiccoopptteerr", "1234567", false)
            ),
            Arguments.of(
                getUser(null, null, null, false),
                getUserModel(null, null, null, false)
            )
        );
    }

    private static User getUser(Long id, String name, String email, boolean enabled) {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(id);
        Mockito.when(user.getName()).thenReturn(name);
        Mockito.when(user.getEmail()).thenReturn(email);
        Mockito.when(user.isEnabled()).thenReturn(enabled);
        return user;
    }

    private static UserModel getUserModel(Long id, String name, String email, boolean enabled) {
        UserModel model = new UserModel();
        model.setId(id);
        model.setName(name);
        model.setEmail(email);
        model.setEnabled(enabled);
        return model;
    }
}
