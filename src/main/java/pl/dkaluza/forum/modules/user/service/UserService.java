package pl.dkaluza.forum.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.exceptions.EmailAlreadyExistException;
import pl.dkaluza.forum.modules.user.mapper.PagedUserMapper;
import pl.dkaluza.forum.modules.user.mapper.UserCreationMapper;
import pl.dkaluza.forum.modules.user.mapper.UserMapper;
import pl.dkaluza.forum.modules.user.models.UserCreationModel;
import pl.dkaluza.forum.modules.user.models.UserModel;
import pl.dkaluza.forum.modules.user.repository.UserRepository;

@Component
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PagedUserMapper pagedUserMapper;
    private final UserCreationMapper userCreationMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PagedUserMapper pagedUserMapper, UserCreationMapper userCreationMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pagedUserMapper = pagedUserMapper;
        this.userCreationMapper = userCreationMapper;
    }

    public PagedModel<UserModel> findAll(Pageable pageable) {
        return pagedUserMapper.toModel(
            userRepository.findAll(pageable)
        );
    }

    public UserModel findById(Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
        return userMapper.toModel(user);
    }

    public UserModel findByEmail(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class, email));
        return userMapper.toModel(user);
    }

    public UserModel create(UserCreationModel model) throws EmailAlreadyExistException {
        if (doesEmailExist(model.getEmail())) {
            throw new EmailAlreadyExistException(model.getEmail());
        }

        User user = userCreationMapper.toObject(model);
        user = userRepository.save(user);
        return userMapper.toModel(user);
    }

    public UserModel update(UserModel model) throws EntityNotFoundException {
        User user = userMapper.toObject(model);
        user = userRepository.save(user);
        return userMapper.toModel(user);
    }

    public UserModel delete(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    private boolean doesEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
