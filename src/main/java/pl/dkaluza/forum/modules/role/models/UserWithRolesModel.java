package pl.dkaluza.forum.modules.role.models;

import org.springframework.hateoas.RepresentationModel;
import pl.dkaluza.forum.modules.user.models.UserModel;

import java.util.List;

public class UserWithRolesModel extends RepresentationModel<UserWithRolesModel> {
    private UserModel user;
    private List<RoleModel> roles;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleModel> roles) {
        this.roles = roles;
    }
}
