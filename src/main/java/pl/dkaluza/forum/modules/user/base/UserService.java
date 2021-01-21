package pl.dkaluza.forum.modules.user.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.core.validator.ComposedValidatorsExecutor;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.events.OnUserDeleteEvent;
import pl.dkaluza.forum.modules.user.base.events.OnUserRegisterEvent;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.models.basic.PagedUserMapper;
import pl.dkaluza.forum.modules.user.base.models.basic.UserMapper;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterMapper;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.base.validators.UserRegisterValidator;

@Component
public class UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final ComposedValidatorsExecutor validatorsExecutor;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PagedUserMapper pagedUserMapper;
    private final UserRegisterMapper userRegisterMapper;

    @Autowired
    public UserService(ApplicationEventPublisher eventPublisher, ComposedValidatorsExecutor validatorsExecutor, UserRepository userRepository, UserMapper userMapper, PagedUserMapper pagedUserMapper, UserRegisterMapper userRegisterMapper) {
        this.eventPublisher = eventPublisher;
        this.validatorsExecutor = validatorsExecutor;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pagedUserMapper = pagedUserMapper;
        this.userRegisterMapper = userRegisterMapper;
    }

    @Transactional(readOnly = true)
    public PagedModel<UserModel> findAll(Pageable pageable) {
        return pagedUserMapper.toModel(
            userRepository.findAll(pageable)
        );
    }

    @Transactional(readOnly = true)
    public UserModel findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toModel(user);
    }

    @Transactional
    public UserModel register(UserRegisterModel model) {
        validatorsExecutor.validate(
            new UserRegisterValidator(model, userRepository)
        );

        User user = userRegisterMapper.toObject(model);
        user = userRepository.save(user);
        eventPublisher.publishEvent(new OnUserRegisterEvent(user));
        return userMapper.toModel(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
        eventPublisher.publishEvent(new OnUserDeleteEvent(id));
    }
}
