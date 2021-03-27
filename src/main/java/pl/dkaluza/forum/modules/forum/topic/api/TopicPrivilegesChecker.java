package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.security.UserDetailsImpl;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicModel;
import pl.dkaluza.forum.modules.forum.topic.services.TopicService;

@Component
class TopicPrivilegesChecker {
    private final TopicService topicService;

    @Autowired
    TopicPrivilegesChecker(TopicService topicService) {
        this.topicService = topicService;
    }

    public boolean canCreateTopic(@Nullable Authentication auth, CreateTopicModel model) {
        if (auth == null) {
            return false;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails.getId() == model.getAuthorId();
    }

    public boolean canOpenTopic(@Nullable Authentication auth, Long id) throws TopicNotFoundException {
        return isTopicAuthor(auth, id);
    }

    public boolean canCloseTopic(@Nullable Authentication auth, Long id) throws TopicNotFoundException {
        return isTopicAuthor(auth, id);
    }

    private boolean isTopicAuthor(@Nullable Authentication auth, Long id) throws TopicNotFoundException {
        if (auth == null) {
            return false;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        TopicModel model = topicService.get(id);
        return userDetails.getId() == model.getAuthorId();
    }
}
