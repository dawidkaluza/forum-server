package pl.dkaluza.forum.modules.forum.topic.api.hateoas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.forum.topic.api.TopicController;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagedTopicModelsAssembler implements RepresentationModelAssembler<Page<TopicModel>, PagedModel<TopicModel>> {
    private final TopicModelAssembler topicModelAssembler;
    private final PagedResourcesAssembler<TopicModel> pagedTopicModelsAssembler;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PagedTopicModelsAssembler(TopicModelAssembler topicModelAssembler, PagedResourcesAssembler<TopicModel> pagedTopicModelsAssembler) {
        this.topicModelAssembler = topicModelAssembler;
        this.pagedTopicModelsAssembler = pagedTopicModelsAssembler;
    }

    @Override
    public PagedModel<TopicModel> toModel(Page<TopicModel> page) {
        PagedModel<TopicModel> model = pagedTopicModelsAssembler.toModel(page, topicModelAssembler);
        model.add(buildCreateTopicLink());
        return model;
    }

    @Override
    public CollectionModel<PagedModel<TopicModel>> toCollectionModel(Iterable<? extends Page<TopicModel>> entities) {
        throw new UnsupportedOperationException("Can't create collection of page object");
    }

    private Link buildCreateTopicLink() {
        return linkTo(methodOn(TopicController.class).create(null, new CreateTopicModel())).withRel("createTopic");
    }
}
