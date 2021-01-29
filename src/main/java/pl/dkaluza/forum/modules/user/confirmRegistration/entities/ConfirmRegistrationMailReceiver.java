package pl.dkaluza.forum.modules.user.confirmRegistration.entities;

import javax.persistence.*;

@Entity
public class ConfirmRegistrationMailReceiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, length = 128)
    private String email;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmRegistrationMailReceiver)) return false;

        ConfirmRegistrationMailReceiver that = (ConfirmRegistrationMailReceiver) o;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
