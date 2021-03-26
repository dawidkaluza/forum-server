package pl.dkaluza.forum.modules.forum.topic.models.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ModelMapper;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;

@Component
public class TopicMapper implements ModelMapper<Pair<Topic, Post>, TopicModel> {
    private final PostMapper postMapper;

    @Autowired
    public TopicMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public TopicModel toModel(Pair<Topic, Post> topicAndPost) {
        TopicModel model = new TopicModel();
        Topic topic = topicAndPost.getFirst();
        model.setId(topic.getId());
        model.setTitle(topic.getTitle());
        model.setClosed(topic.isClosed());
        Post post = topicAndPost.getSecond();
        model.setCreatedAt(post.getCreatedAt());
        model.setAuthorId(post.getAuthor().getId());
        model.setLastPost(postMapper.toModel(post));
        return model;
    }
}
