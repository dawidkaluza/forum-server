package pl.dkaluza.forum.modules.user.base.models.register;

import pl.dkaluza.forum.core.validation.ExternalEmail;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterModel {
    @Pattern(regexp = "^[\\S]{3,32}$", message = "{user.registration.invalidName}")
    private String name;

    @ExternalEmail(message = "{user.registration.invalidEmail}")
    @Size(max = 128, message = "{user.registration.invalidEmail}")
    private String email;

    @Pattern(regexp = "^[\\S]{5,32}$", message = "{user.registration.invalidPassword}")
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
