package pl.dkaluza.forum.modules.forum.topic.api.hateoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.forum.topic.api.PostController;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagedTopicPostsModelsAssembler implements RepresentationModelAssembler<Page<PostModel>, PagedModel<PostModel>> {
    private final PostModelAssembler postModelAssembler;
    private final PagedResourcesAssembler<PostModel> pagedPostModelsAssembler;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PagedTopicPostsModelsAssembler(PostModelAssembler postModelAssembler, PagedResourcesAssembler<PostModel> pagedPostModelsAssembler) {
        this.postModelAssembler = postModelAssembler;
        this.pagedPostModelsAssembler = pagedPostModelsAssembler;
    }

    @Override
    public PagedModel<PostModel> toModel(Page<PostModel> page) {
        PagedModel<PostModel> pagedModel = pagedPostModelsAssembler.toModel(page, postModelAssembler);
        pagedModel.add(buildCreatePostLink());
        return pagedModel;
    }

    @Override
    public CollectionModel<PagedModel<PostModel>> toCollectionModel(Iterable<? extends Page<PostModel>> entities) {
        throw new UnsupportedOperationException("Can't create collection of page object");
    }

    private Link buildCreatePostLink() {
        return linkTo(methodOn(PostController.class).create(null, new CreatePostModel())).withRel("createPost");
    }
}
