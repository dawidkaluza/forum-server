package pl.dkaluza.forum.modules.user.base.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class UserPropertiesSupplier {
    private final Environment environment;

    @Autowired
    public UserPropertiesSupplier(Environment environment) {
        this.environment = environment;
    }

    public Duration getTimeToActivateAccount() {
        return environment.getProperty("user.days-to-activate-account", Duration.class, Duration.ofDays(7));
    }
}
