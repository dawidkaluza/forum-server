package pl.dkaluza.forum.modules.user.base.events;

import org.springframework.context.ApplicationEvent;

public class OnUserDeleteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6776535999795765894L;
    private final long userId;

    public OnUserDeleteEvent(long userId) {
        super(userId);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
