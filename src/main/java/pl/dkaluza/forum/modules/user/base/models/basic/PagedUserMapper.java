package pl.dkaluza.forum.modules.user.base.models.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.PagedModelMapper;
import pl.dkaluza.forum.modules.user.base.UserController;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagedUserMapper extends PagedModelMapper<User, UserModel> {
    @Autowired
    public PagedUserMapper(UserMapper mapper) {
        super(mapper);
    }

    @Override
    protected Iterable<Link> getLinks(Page<User> page) {
        return Collections.singleton(
            buildSelfLink(page)
        );
    }

    private Link buildSelfLink(Page<User> page) {
        try {
            return linkTo(methodOn(UserController.class).findAll(Pageable.unpaged())).withSelfRel()
                .andAffordance(
                    afford(
                        methodOn(UserController.class).register(new UserRegisterModel())
                    )
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Link> buildPageLinks(Page<User> page) {
        List<Link> links = new ArrayList<>();

        if (page.getNumber() != 0) {
            //TODO paste "size" if exists
            links.add(
                linkTo(methodOn(UserController.class).findAll(Pageable.unpaged())).withRel(IanaLinkRelations.FIRST)
            );
        }

        if (page.getTotalPages() > 1) {
            String uri = linkTo(methodOn(UserController.class).findAll(null))
                .toUriComponentsBuilder()
                .queryParam("page", page.getTotalPages() - 1)
                .queryParam("size", page.getSize())
                .build().toString();

            links.add(
                Link.of(uri, IanaLinkRelations.LAST)
            );
        }

        if (page.hasPrevious()) {
            String uri = linkTo(methodOn(UserController.class).findAll(null))
                .toUriComponentsBuilder()
                .queryParam("page", page.getNumber() - 1)
                .queryParam("size", page.getSize())
                .build().toString();

            links.add(
                Link.of(uri, IanaLinkRelations.PREV)
            );
        }

        if (page.hasNext()) {
            String uri = linkTo(methodOn(UserController.class).findAll(null))
                .toUriComponentsBuilder()
                .queryParam("page", page.getNumber() + 1)
                .queryParam("size", page.getSize())
                .build().toString();

            links.add(
                Link.of(uri, IanaLinkRelations.NEXT)
            );
        }

        return links;
    }
}
