package pl.dkaluza.forum.modules.user.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public PagedModel<UserModel> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserModel register(@RequestBody UserRegisterModel model) {
        return userService.register(model);
    }

    @GetMapping("/user/{id}")
    public UserModel get(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserModel delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return null;
    }
}
