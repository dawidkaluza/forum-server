package pl.dkaluza.forum.modules.userDetails.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dkaluza.forum.modules.role.core.UserWithRoles;
import pl.dkaluza.forum.modules.role.entities.UserRole;
import pl.dkaluza.forum.modules.role.repository.UserRoleRepository;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.userDetails.data.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found in database"));

        List<UserRole> userRoles = userRoleRepository.findAllByUserAndFetchRolesEagerly(user);
        UserWithRoles userWithRoles = new UserWithRoles();
        userWithRoles.setUser(user);
        userWithRoles.setRoles(
            userRoles.stream().map(UserRole::getRole).collect(Collectors.toList())
        );
        return UserDetailsImpl.ofUserWithRoles(userWithRoles);
    }
}
