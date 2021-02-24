package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.user.base.api.hateoas.RegisterModelAssembler;
import pl.dkaluza.forum.modules.user.base.api.hateoas.UserModelAssembler;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.services.UserService;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final RegisterModelAssembler registerModelAssembler;
    private final PagedResourcesAssembler<UserModel> pagedUsersModelsAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, RegisterModelAssembler registerModelAssembler, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PagedResourcesAssembler<UserModel> pagedUsersModelsAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.registerModelAssembler = registerModelAssembler;
        this.pagedUsersModelsAssembler = pagedUsersModelsAssembler;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserModel register(@Valid @RequestBody UserRegisterModel userRegisterModel) {
        return registerModelAssembler.toModel(
            userService.register(userRegisterModel)
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
}
