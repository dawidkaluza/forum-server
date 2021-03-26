package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.security.UserDetailsImpl;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;

@Component
class PostPrivilegesChecker {
    public boolean canCreatePost(Authentication auth, CreatePostModel model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return model.getAuthorId() == userDetails.getId();
    }
}
