package pl.dkaluza.forum.modules.user.mapper;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.PagedModelMapper;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.models.UserModel;

import java.util.Collections;

@Component
public class PagedUserMapper extends PagedModelMapper<User, UserModel> {
    public PagedUserMapper(UserMapper mapper) {
        super(mapper);
    }

    @Override
    protected Iterable<Link> getLinks() {
        return Collections.singleton(buildLinks());
    }

    private Link buildLinks() {
        //TODO
        return null;
    }
}
