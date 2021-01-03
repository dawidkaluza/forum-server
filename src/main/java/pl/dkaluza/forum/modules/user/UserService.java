package pl.dkaluza.forum.modules.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.core.exceptions.ApiRequestException;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.core.validator.ComposedValidatorsExecutor;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.models.basic.PagedUserMapper;
import pl.dkaluza.forum.modules.user.models.create.UserCreationMapper;
import pl.dkaluza.forum.modules.user.models.basic.UserMapper;
import pl.dkaluza.forum.modules.user.models.create.UserCreationModel;
import pl.dkaluza.forum.modules.user.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.validators.CreateUserValidator;

@Component
public class UserService {
    private final ComposedValidatorsExecutor validatorsExecutor;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PagedUserMapper pagedUserMapper;
    private final UserCreationMapper userCreationMapper;

    @Autowired
    public UserService(ComposedValidatorsExecutor validatorsExecutor, UserRepository userRepository, UserMapper userMapper, PagedUserMapper pagedUserMapper, UserCreationMapper userCreationMapper) {
        this.validatorsExecutor = validatorsExecutor;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pagedUserMapper = pagedUserMapper;
        this.userCreationMapper = userCreationMapper;
    }

    @Transactional(readOnly = true)
    public PagedModel<UserModel> findAll(Pageable pageable) {
        return pagedUserMapper.toModel(
            userRepository.findAll(pageable)
        );
    }

    @Transactional(readOnly = true)
    public UserModel findById(Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
        return userMapper.toModel(user);
    }

    @Transactional
    public UserModel create(UserCreationModel model) throws ApiRequestException {
        validatorsExecutor.validate(
            new CreateUserValidator(model, userRepository)
        );

        User user = userCreationMapper.toObject(model);
        user = userRepository.save(user);
        return userMapper.toModel(user);
    }

    @Transactional
    public UserModel update(UserModel model) throws EntityNotFoundException {
        User user = userMapper.toObject(model);
        user = userRepository.save(user);
        return userMapper.toModel(user);
    }

    @Transactional
    public UserModel delete(Long id) {
        userRepository.deleteById(id);
        return null;
    }
}
