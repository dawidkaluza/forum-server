package pl.dkaluza.forum.modules.user.base.models.register;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserRegisterModel {
    @Pattern(regexp = "^[^\\S]{3,32}$", message = "{user.register.invalidName}")
    @NotEmpty(message = "{user.register.emptyName")
    private String name;

    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "{user.register.invalidEmail}")
    @NotEmpty(message = "{user.register.emptyEmail}")
    private String email;

    @Pattern(regexp = "^[^\\S]{5,32}$", message = "{user.register.invalidPassword}")
    @NotEmpty(message = "{user.register.emptyPassword")
    private String plainPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
}
