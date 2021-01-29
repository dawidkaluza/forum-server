package pl.dkaluza.forum.modules.user.confirmRegistration.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ConfirmRegistrationPropertiesSupplier {
    private final Environment environment;

    @Autowired
    public ConfirmRegistrationPropertiesSupplier(Environment environment) {
        this.environment = environment;
    }

    public Duration tokenExpiration() {
        return Duration.ofHours(
            environment.getProperty("user.confirm-registration.token.expiration-hours", Integer.class, 24)
        );
    }

    public int getMaxTries() {
        return environment.getProperty("user.confirm-registration.resend-token.max-tries", Integer.class, 5);
    }

    public Duration tryExpiration() {
        return Duration.ofMinutes(
            environment.getProperty("user.confirm-registration.resend-token.try-expiration-minutes", Integer.class, 60)
        );
    }
}
