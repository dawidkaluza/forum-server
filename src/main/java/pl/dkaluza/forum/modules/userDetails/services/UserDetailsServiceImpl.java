package pl.dkaluza.forum.modules.userDetails.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.modules.role.mappers.UserWithRolesMapper;
import pl.dkaluza.forum.modules.role.models.UserWithRolesModel;
import pl.dkaluza.forum.modules.role.servies.RoleService;
import pl.dkaluza.forum.modules.user.models.UserModel;
import pl.dkaluza.forum.modules.user.service.UserService;
import pl.dkaluza.forum.modules.userDetails.data.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RoleService roleService;
    private final UserService userService;
    private final UserWithRolesMapper userWithRolesMapper;

    @Autowired
    public UserDetailsServiceImpl(RoleService roleService, UserService userService, UserWithRolesMapper userWithRolesMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.userWithRolesMapper = userWithRolesMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserModel userModel = userService.findByEmail(email);
            UserWithRolesModel userWithRolesModel = roleService.findUserRoles(userModel);
            return UserDetailsImpl.ofUserWithRoles(
                userWithRolesMapper.toObject(userWithRolesModel)
            );
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
