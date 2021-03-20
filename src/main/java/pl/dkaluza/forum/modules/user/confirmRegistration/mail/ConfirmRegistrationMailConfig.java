package pl.dkaluza.forum.modules.user.confirmRegistration.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

@Configuration
public class ConfirmRegistrationMailConfig {
    @Bean("confirmRegistrationMailSenderExecutor")
    public TaskExecutor confirmRegistrationMailSenderExecutor() {
        return new ConcurrentTaskExecutor();
    }
}
