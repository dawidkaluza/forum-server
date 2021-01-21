package pl.dkaluza.forum.modules.role.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;
import pl.dkaluza.forum.core.mappers.ModelAndObjectMapper;
import pl.dkaluza.forum.modules.role.entities.Role;
import pl.dkaluza.forum.modules.role.models.RoleModel;
import pl.dkaluza.forum.modules.role.repository.RoleRepository;

@Component
public class RoleMapper implements ModelAndObjectMapper<Role, RoleModel> {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role toObject(RoleModel model) throws EntityNotFoundException {
        Role role = roleRepository.findById(model.getId()).orElseThrow(() -> new EntityNotFoundException(Role.class, model.getId()));
        role.setName(model.getName());
        role.setStyle(model.getStyle());
        return role;
    }

    @Override
    public RoleModel toModel(Role entity) {
        RoleModel model = new RoleModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setStyle(entity.getStyle());
        return model;
    }
}
