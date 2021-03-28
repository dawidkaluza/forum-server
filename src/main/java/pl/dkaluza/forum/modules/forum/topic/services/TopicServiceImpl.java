package pl.dkaluza.forum.modules.forum.topic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;
import pl.dkaluza.forum.modules.forum.topic.exceptions.PostNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostMapper;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicMapper;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicMapper;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicModel;
import pl.dkaluza.forum.modules.forum.topic.repositories.PostRepository;
import pl.dkaluza.forum.modules.forum.topic.repositories.TopicRepository;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

@Component
class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final TopicMapper topicMapper;
    private final CreateTopicMapper createTopicMapper;
    private final PostMapper postMapper;

    @Autowired
    TopicServiceImpl(TopicRepository topicRepository, PostRepository postRepository, TopicMapper topicMapper, CreateTopicMapper createTopicMapper, PostMapper postMapper) {
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
        this.topicMapper = topicMapper;
        this.createTopicMapper = createTopicMapper;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional
    public TopicModel create(CreateTopicModel model) throws UserNotFoundException {
        Pair<Topic, Post> pair = createTopicMapper.toObject(model);
        Topic topic = pair.getFirst();
        topicRepository.save(topic);
        Post post = pair.getSecond();
        postRepository.save(post);
        return topicMapper.toModel(Pair.of(topic, post));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicModel> getAll(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by(Sort.Direction.DESC, "id")
        );

        Page<TopicModel> page = topicRepository
            .findAll(pageRequest)
            .map(topic -> {
                Post post = postRepository
                    .findFirstByTopicOrderById(topic)
                    .orElseThrow(() -> new PostNotFoundException("Can't find first post for topic with id=" + topic.getId()));

                return topicMapper.toModel(Pair.of(topic, post));
            });

        return new PageImpl<>(
            page.getContent(), pageable, page.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TopicModel get(Long id) throws TopicNotFoundException {
        Topic topic = topicRepository
            .findById(id)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + id));

        Post post = postRepository
            .findFirstByTopicOrderById(topic)
            .orElseThrow(() -> new PostNotFoundException("Can't find first post for topic with id=" + topic.getId()));

        return topicMapper.toModel(Pair.of(topic, post));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostModel> getPosts(Long id, Pageable pageable) throws TopicNotFoundException {
        Topic topic = topicRepository
            .findById(id)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + id));

        return postRepository
            .findAllByTopicOrderById(topic, pageable)
            .map(postMapper::toModel);
    }

    @Override
    @Transactional
    public TopicModel close(Long id) throws TopicNotFoundException {
        Topic topic = topicRepository
            .findById(id)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + id));

        topic.setClosed(true);
        topicRepository.save(topic);

        Post post = postRepository
            .findFirstByTopicOrderById(topic)
            .orElseThrow(() -> new PostNotFoundException("Can't find first post for topic with id=" + topic.getId()));

        return topicMapper.toModel(Pair.of(topic, post));
    }

    @Override
    @Transactional
    public TopicModel open(Long id) throws TopicNotFoundException {
        Topic topic = topicRepository
            .findById(id)
            .orElseThrow(() -> new TopicNotFoundException("Can't find topic with id=" + id));

        topic.setClosed(false);
        topicRepository.save(topic);

        Post post = postRepository
            .findFirstByTopicOrderById(topic)
            .orElseThrow(() -> new PostNotFoundException("Can't find first post for topic with id=" + topic.getId()));

        return topicMapper.toModel(Pair.of(topic, post));
    }
}
