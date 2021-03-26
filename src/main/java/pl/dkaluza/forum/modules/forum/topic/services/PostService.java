package pl.dkaluza.forum.modules.forum.topic.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dkaluza.forum.modules.forum.topic.exceptions.PostNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

public interface PostService {
    PostModel create(CreatePostModel model) throws TopicNotFoundException, UserNotFoundException;

    Page<PostModel> getAll(Pageable pageable);

    PostModel get(Long id) throws PostNotFoundException;
}
