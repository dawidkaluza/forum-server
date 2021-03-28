package pl.dkaluza.forum.modules.user.base.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.properties.UserPropertiesSupplier;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.base.services.UserService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class RemoveNotEnabledUsersScheduler {
    private final UserPropertiesSupplier propertiesSupplier;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public RemoveNotEnabledUsersScheduler(UserPropertiesSupplier propertiesSupplier, UserService userService, UserRepository userRepository) {
        this.propertiesSupplier = propertiesSupplier;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void removeNotEnabledUsers() {
        List<User> users = userRepository.findAllByCreatedAtBeforeAndEnabledIsFalse(
            LocalDateTime.now(ZoneOffset.UTC).minus(propertiesSupplier.getTimeToActivateAccount())
        );

        for (User user : users) {
            userService.delete(user.getId());
        }
    }
}
