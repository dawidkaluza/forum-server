package pl.dkaluza.forum.modules.userDetails.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dkaluza.forum.modules.role.core.UserWithRoles;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private final UserWithRoles userWithRoles;

    private UserDetailsImpl(UserWithRoles userWithRoles) {
        this.userWithRoles = userWithRoles;
    }

    public static UserDetailsImpl ofUserWithRoles(UserWithRoles userWithRoles) {
        return new UserDetailsImpl(userWithRoles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userWithRoles.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return userWithRoles.getUser().getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return userWithRoles.getUser().getEmail();
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
        return false;
    }
}
