package pl.dkaluza.forum.modules.forum.topic.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.forum.topic.api.TopicController;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TopicModelAssembler implements RepresentationModelAssembler<TopicModel, TopicModel> {
    @Override
    public TopicModel toModel(TopicModel model) {
        model.add(buildSelfLink(model));
        model.add(buildPostsLink(model));
        model.add(buildOpenTopicLink(model));
        model.add(buildCloseTopicLink(model));
        return model;
    }

    private Link buildSelfLink(TopicModel model) {
        return linkTo(methodOn(TopicController.class).get(model.getId())).withSelfRel();
    }

    private Link buildPostsLink(TopicModel model) {
        return linkTo(methodOn(TopicController.class).getPosts(model.getId(), null)).withRel("topicPosts");
    }

    private Link buildOpenTopicLink(TopicModel model) {
        return linkTo(methodOn(TopicController.class).open(null, model.getId())).withRel("openTopic");
    }

    private Link buildCloseTopicLink(TopicModel model) {
        return linkTo(methodOn(TopicController.class).close(null, model.getId())).withRel("closeTopic");
    }
}
