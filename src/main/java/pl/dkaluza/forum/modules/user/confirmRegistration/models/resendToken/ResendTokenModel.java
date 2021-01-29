package pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken;

import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "resendToken")
public class ResendTokenModel {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
