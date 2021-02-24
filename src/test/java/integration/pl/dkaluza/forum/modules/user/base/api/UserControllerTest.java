package pl.dkaluza.forum.modules.user.base.api;

public class UserControllerTest {

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
