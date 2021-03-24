package pl.dkaluza.forum.core.security;

import pl.dkaluza.forum.core.validation.ExternalEmail;

import javax.validation.constraints.NotNull;

public class LoginModel {
    @NotNull(message = "security.authentication.badCredentials")
    @ExternalEmail(message = "security.authentication.badCredentials")
    private String email;

    @NotNull(message = "security.authentication.badCredentials")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
