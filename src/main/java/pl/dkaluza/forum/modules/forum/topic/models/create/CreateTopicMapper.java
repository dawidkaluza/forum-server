package pl.dkaluza.forum.modules.forum.topic.models.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ObjectMapper;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

import java.time.LocalDateTime;

@Component
public class CreateTopicMapper implements ObjectMapper<Pair<Topic, Post>, CreateTopicModel> {
    private final UserRepository userRepository;

    @Autowired
    public CreateTopicMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Pair<Topic, Post> toObject(CreateTopicModel model) throws UserNotFoundException {
        Topic topic = new Topic();
        topic.setTitle(model.getTitle());
        topic.setClosed(false);

        Post post = new Post();
        post.setContent(model.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setTopic(topic);

        long authorId = model.getAuthorId();
        User author = userRepository
            .findById(authorId)
            .orElseThrow(() -> new UserNotFoundException("Can't find author with id=" + authorId));
        post.setAuthor(author);
        return Pair.of(topic, post);
    }
}
