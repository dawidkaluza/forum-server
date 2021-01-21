package pl.dkaluza.forum.modules.role.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;
import pl.dkaluza.forum.core.mappers.ModelAndObjectMapper;
import pl.dkaluza.forum.modules.role.core.UserWithRoles;
import pl.dkaluza.forum.modules.role.entities.Role;
import pl.dkaluza.forum.modules.role.models.RoleModel;
import pl.dkaluza.forum.modules.role.models.UserWithRolesModel;
import pl.dkaluza.forum.modules.user.base.models.basic.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserWithRolesMapper implements ModelAndObjectMapper<UserWithRoles, UserWithRolesModel> {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public UserWithRolesMapper(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserWithRoles toObject(UserWithRolesModel model) throws EntityNotFoundException {
        UserWithRoles userWithRoles = new UserWithRoles();

        userWithRoles.setUser(
            userMapper.toObject(model.getUser())
        );

        List<Role> roles = new ArrayList<>();
        for (RoleModel roleModel : model.getRoles()) {
            roles.add(
                roleMapper.toObject(roleModel)
            );
        }
        userWithRoles.setRoles(roles);
        return userWithRoles;
    }

    @Override
    public UserWithRolesModel toModel(UserWithRoles object) {
        UserWithRolesModel model = new UserWithRolesModel();
        model.setUser(
            userMapper.toModel(object.getUser())
        );
        List<RoleModel> rolesModels = object.getRoles()
            .stream()
            .map(roleMapper::toModel)
            .collect(Collectors.toList());
        model.setRoles(rolesModels);
        return model;
    }
}
