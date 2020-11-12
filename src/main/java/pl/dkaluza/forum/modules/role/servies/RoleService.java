package pl.dkaluza.forum.modules.role.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.modules.role.core.UserWithRoles;
import pl.dkaluza.forum.modules.role.entities.Role;
import pl.dkaluza.forum.modules.role.entities.UserRole;
import pl.dkaluza.forum.modules.role.mappers.UserWithRolesMapper;
import pl.dkaluza.forum.modules.role.models.UserWithRolesModel;
import pl.dkaluza.forum.modules.role.repository.UserRoleRepository;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.mapper.UserMapper;
import pl.dkaluza.forum.modules.user.models.UserModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final UserWithRolesMapper userWithRolesMapper;

    @Autowired
    public RoleService(UserRoleRepository userRoleRepository, UserMapper userMapper, UserWithRolesMapper userWithRolesMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userMapper = userMapper;
        this.userWithRolesMapper = userWithRolesMapper;
    }

    public UserWithRolesModel findUserRoles(UserModel userModel) throws EntityNotFoundException {
        User user = userMapper.toObject(userModel);
        List<UserRole> userRoles = userRoleRepository.findAllByUserAndFetchRolesEagerly(user);
        List<Role> roles = userRoles
            .stream()
            .map(UserRole::getRole)
            .collect(Collectors.toList());

        UserWithRoles userWithRoles = new UserWithRoles();
        userWithRoles.setUser(user);
        userWithRoles.setRoles(roles);
        return userWithRolesMapper.toModel(userWithRoles);
    }
}
