package pl.dkaluza.forum.modules.forum.topic.models.basic;

import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ModelMapper;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class PostMapper implements ModelMapper<Post, PostModel> {
    @Override
    public PostModel toModel(Post post) {
        PostModel model = new PostModel();
        model.setId(post.getId());
        model.setContent(post.getContent());
        model.setCreatedAt(ZonedDateTime.of(post.getCreatedAt(), ZoneId.systemDefault()));
        model.setTopicId(post.getTopic().getId());
        model.setAuthorId(post.getAuthor().getId());
        return model;
    }
}
