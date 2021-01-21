package pl.dkaluza.forum.modules.user.base.events;

import org.springframework.context.ApplicationEvent;
import pl.dkaluza.forum.modules.user.base.entities.User;

public class OnUserRegisterEvent extends ApplicationEvent {
    private static final long serialVersionUID = -7950664606923832193L;

    public OnUserRegisterEvent(User user) {
        super(user);
    }

    public User getUser() {
        return (User) getSource();
    }
}
