package pl.dkaluza.forum.modules.user.base.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;

public interface UserService {
    Page<UserModel> findAll(Pageable pageable);

    UserModel findById(Long id);

    UserModel register(UserRegisterModel model);

    void delete(Long id);
}
