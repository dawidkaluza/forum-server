package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.user.base.api.hateoas.UserModelAssembler;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.services.UserService;

@RestController
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<UserModel> pagedUsersModelsAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PagedResourcesAssembler<UserModel> pagedUsersModelsAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.pagedUsersModelsAssembler = pagedUsersModelsAssembler;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserModel register(@RequestBody UserRegisterModel model) {
        return userModelAssembler.toModel(
            userService.register(model)
        );
    }

    @GetMapping("/user")
    public PagedModel<UserModel> findAll(Pageable pageable) {
        return pagedUsersModelsAssembler.toModel(
            userService.findAll(pageable), userModelAssembler
        );
    }

    @GetMapping("/user/{id}")
    public UserModel get(@PathVariable("id") Long id) {
        return userModelAssembler.toModel(
            userService.findById(id)
        );
    }

//    @DeleteMapping("/user/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable("id") Long id) {
//        userService.delete(id);
//    }
}
