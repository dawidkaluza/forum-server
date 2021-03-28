package pl.dkaluza.forum.modules.forum.topic.models.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ObjectMapper;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.repositories.TopicRepository;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CreatePostMapper implements ObjectMapper<Post, CreatePostModel> {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public CreatePostMapper(UserRepository userRepository, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public Post toObject(CreatePostModel model) {
        Post post = new Post();
        post.setContent(model.getContent());
        post.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

        long topicId = model.getTopicId();
        Topic topic = topicRepository
            .findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + topicId));
        post.setTopic(topic);

        long authorId = model.getAuthorId();
        User author = userRepository
            .findById(authorId)
            .orElseThrow(() -> new UserNotFoundException("Can't find author with id=" + authorId));
        post.setAuthor(author);
        return post;
    }
}
