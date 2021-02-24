package pl.dkaluza.forum.modules.user.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.events.OnUserDeleteEvent;
import pl.dkaluza.forum.modules.user.base.events.OnUserRegisterEvent;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.models.basic.UserMapper;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterMapper;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRegisterMapper userRegisterMapper;

    @Autowired
    public UserServiceImpl(ApplicationEventPublisher eventPublisher, UserRepository userRepository, UserMapper userMapper, UserRegisterMapper userRegisterMapper) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRegisterMapper = userRegisterMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository
            .findAll(pageable)
            .map(userMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toModel(user);
    }

    @Override
    @Transactional
    public UserModel register(UserRegisterModel model) {
        if (userRepository.existsByEmail(model.getEmail())) {
            throw new EmailAlreadyExistsException(model.getEmail());
        }

        if (userRepository.existsByName(model.getName())) {
            throw new NameAlreadyExistsException(model.getName());
        }

        User user = userRegisterMapper.toObject(model);
        user = userRepository.save(user);
        eventPublisher.publishEvent(new OnUserRegisterEvent(user.getId()));
        return userMapper.toModel(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }

        eventPublisher.publishEvent(new OnUserDeleteEvent(id));
    }
}
