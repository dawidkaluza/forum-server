package pl.dkaluza.forum.modules.user.base.models.resetPassword;

import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "passwordReset")
public class ResetPasswordModel {
    private String email;

    public ResetPasswordModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
