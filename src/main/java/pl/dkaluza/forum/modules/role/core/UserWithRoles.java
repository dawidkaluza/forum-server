package pl.dkaluza.forum.modules.role.core;

import pl.dkaluza.forum.modules.role.entities.Role;
import pl.dkaluza.forum.modules.user.entities.User;

import java.util.List;

public class UserWithRoles {
    private User user;
    private List<Role> roles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserWithRoles)) return false;

        UserWithRoles that = (UserWithRoles) o;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public String toString() {
        return "UserWithRoles{" +
            "user=" + user +
            '}';
    }
}
