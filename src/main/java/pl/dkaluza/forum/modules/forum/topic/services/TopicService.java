package pl.dkaluza.forum.modules.forum.topic.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicModel;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

public interface TopicService {
    TopicModel create(CreateTopicModel model) throws UserNotFoundException;

    Page<TopicModel> getAll(Pageable pageable);

    TopicModel get(Long id) throws TopicNotFoundException;

    Page<PostModel> getPosts(Long id, Pageable pageable) throws TopicNotFoundException;

    TopicModel close(Long id) throws TopicNotFoundException;

    TopicModel open(Long id) throws TopicNotFoundException;
}
