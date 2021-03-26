package pl.dkaluza.forum.modules.forum.topic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicClosedException;
import pl.dkaluza.forum.modules.forum.topic.exceptions.PostNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostMapper;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostMapper;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;
import pl.dkaluza.forum.modules.forum.topic.repositories.PostRepository;
import pl.dkaluza.forum.modules.forum.topic.repositories.TopicRepository;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

@Service
class PostServiceImpl implements PostService {
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CreatePostMapper createPostMapper;

    @Autowired
    PostServiceImpl(TopicRepository topicRepository, PostRepository postRepository, PostMapper postMapper, CreatePostMapper createPostMapper) {
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.createPostMapper = createPostMapper;
    }

    @Override
    @Transactional
    public PostModel create(CreatePostModel model) throws TopicNotFoundException, UserNotFoundException {
        long topicId = model.getTopicId();
        Topic topic = topicRepository
            .findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + topicId));

        if (topic.isClosed()) {
            throw new TopicClosedException("Topic with id=" + topicId + " is closed");
        }

        Post post = createPostMapper.toObject(model);
        postRepository.save(post);
        return postMapper.toModel(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostModel> getAll(Pageable pageable) {
        return postRepository
            .findAll(pageable)
            .map(postMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PostModel get(Long id) throws PostNotFoundException {
        Post post = postRepository
            .findById(id)
            .orElseThrow(() -> new PostNotFoundException("Post with id=" + id + " doesn't exist"));

        return postMapper.toModel(post);
    }
}
