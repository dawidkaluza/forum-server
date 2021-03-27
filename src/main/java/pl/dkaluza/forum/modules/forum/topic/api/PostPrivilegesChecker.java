package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.security.UserDetailsImpl;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;

@Component
class PostPrivilegesChecker {
    public boolean canCreatePost(@Nullable Authentication auth, CreatePostModel model) {
        if (auth == null) {
            return false;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return model.getAuthorId() == userDetails.getId();
    }
}
