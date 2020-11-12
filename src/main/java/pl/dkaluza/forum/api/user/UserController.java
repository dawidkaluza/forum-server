package pl.dkaluza.forum.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.modules.user.exceptions.EmailAlreadyExistException;
import pl.dkaluza.forum.modules.user.models.UserCreationModel;
import pl.dkaluza.forum.modules.user.models.UserModel;
import pl.dkaluza.forum.modules.user.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public PagedModel<UserModel> findAll(@PageableDefault Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping("/user")
    public UserModel create(UserCreationModel model) throws EmailAlreadyExistException {
        return userService.create(model);
    }

    @GetMapping("/user/{id}")
    public UserModel get(@PathVariable("id") Long id) throws EntityNotFoundException {
        return userService.findById(id);
    }

    @PutMapping("/user")
    public UserModel update(UserModel model) throws EntityNotFoundException {
        return userService.update(model);
    }

    @DeleteMapping("/user/{id}")
    public UserModel delete(@PathVariable("id") Long id) {
        return userService.delete(id);
    }
}
