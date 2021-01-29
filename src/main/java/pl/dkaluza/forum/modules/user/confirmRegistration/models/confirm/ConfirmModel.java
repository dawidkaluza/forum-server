package pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm;

import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "confirm")
public class ConfirmModel {
    private long id;
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
