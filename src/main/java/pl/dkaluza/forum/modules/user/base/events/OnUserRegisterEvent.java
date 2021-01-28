package pl.dkaluza.forum.modules.user.base.events;

import org.springframework.context.ApplicationEvent;

public class OnUserRegisterEvent extends ApplicationEvent {
    private static final long serialVersionUID = -7950664606923832193L;
    private final long userId;

    public OnUserRegisterEvent(long userId) {
        super(userId);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
