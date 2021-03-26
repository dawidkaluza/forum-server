package pl.dkaluza.forum.modules.forum.topic.api.hateoas;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.forum.topic.api.PostController;
import pl.dkaluza.forum.modules.forum.topic.api.TopicController;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<PostModel, PostModel> {
    @Override
    public PostModel toModel(PostModel model) {
        model.add(buildSelfLink(model));
        model.add(buildTopicLink(model));
        return model;
    }

    private Link buildSelfLink(PostModel model) {
        return linkTo(methodOn(PostController.class).get(model.getId())).withSelfRel();
    }

    private Link buildTopicLink(PostModel model) {
        return linkTo(methodOn(TopicController.class).get(model.getTopicId())).withRel("topic");
    }
}
