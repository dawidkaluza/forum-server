package pl.dkaluza.forum.modules.user.confirmRegistration.entities;

import pl.dkaluza.forum.modules.user.base.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ConfirmRegistrationToken {
    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    private String token;

    private LocalDateTime expiresAt;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmRegistrationToken)) return false;

        ConfirmRegistrationToken that = (ConfirmRegistrationToken) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
