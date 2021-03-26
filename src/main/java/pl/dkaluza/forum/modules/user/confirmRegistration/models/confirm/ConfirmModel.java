package pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ConfirmModel {
    @NotNull(message = "{emptyField}")
    private Long id;

    @NotBlank(message = "{emptyField}")
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
