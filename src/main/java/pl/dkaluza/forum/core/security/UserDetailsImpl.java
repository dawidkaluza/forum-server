package pl.dkaluza.forum.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 9086485465190374880L;

    private final long id;
    private final String username;
    private final String encodedPassword;
    private final boolean enabled;

    private UserDetailsImpl(long id, String username, String encodedPassword, boolean enabled) {
        this.id = id;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.enabled = enabled;
    }

    public static UserDetailsImpl ofUser(User user) {
        return new UserDetailsImpl(
            user.getId(), user.getEmail(), user.getEncodedPassword(), user.isEnabled()
        );
    }

    public long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
            new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
