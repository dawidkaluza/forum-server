package pl.dkaluza.forum.modules.user.base.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.services.UserService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public class UserControllerTest {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private MockMvc mockMvc;

    @Autowired
    public UserControllerTest(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @BeforeEach
    public void setUp(WebApplicationContext webAppContext, RestDocumentationContextProvider restDoc) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(documentationConfiguration(restDoc))
            .build();

        UserRegisterModel model = new UserRegisterModel();
        model.setEmail("mark@gmail.com");
        model.setName("mark");
        model.setPlainPassword("veryHardPassword");
        userService.register(model);
    }

    @Test
    public void register_emailAlreadyExists_responseWithProperError() throws Exception {
        //Given
        Map<String, Object> body = new HashMap<>();
        body.put("name", "adam");
        body.put("email", "mark@gmail.com");
        body.put("plainPassword", "adamPswrd");

        //When
        ResultActions result = mockMvc.perform(
            post("/register")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .locale(Locale.ENGLISH)
        );

        //Then
        result
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.fieldErrors[*].field").value("email"))
            .andExpect(jsonPath("$.fieldErrors[*].message").exists());

        //Document
        result.andDo(document("user/register",
            responseBody()
        ));
    }

    @Test
    public void register_invalidEmail_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooShortEmail_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooLongEmail_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_nameAlreadyExists_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_invalidName_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooShortName_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooLongName_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooShortPassword_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_tooLongPassword_responseWithProperError() {
        //TODO
    }

    @Test
    public void register_validData_responseWithCreatedUser() {
        //TODO
    }

    /// CASES WHILE REGISTRATION ////
    /*

    @Test
    public void validate_emailAlreadyExists_resultHasExistsFieldError() {
//        //Given
//        UserRegisterModel model = getUserRegisterModel("Hector", "existing@mail.com", "validPassword");
//        UserRepository repository = getUserRepository();
//        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(model, "userRegisterModel");
//        UserRegisterValidator validator = new UserRegisterValidator(repository);
//
//        //When
//        validator.validate(model, bindingResult);
//
//        //Then
//        FieldError error = bindingResult.getFieldError("email");
//        assertNotNull(error);
//        assertEquals(error.getCode(), "exists");
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
        //TODO
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
        //TODO
    }

    @Test
    public void validate_tooLongPassword_throwException() {
        //TODO
    }

    @Test
    public void validate_fullyCorrectModel_noException() {
        //TODO
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
     */
}
