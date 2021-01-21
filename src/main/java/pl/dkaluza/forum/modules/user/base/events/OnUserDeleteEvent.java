package pl.dkaluza.forum.modules.user.base.events;

import org.springframework.context.ApplicationEvent;

public class OnUserDeleteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6776535999795765894L;

    public OnUserDeleteEvent(Long userId) {
        super(userId);
    }

    public Long getUserId() {
        return (Long) getSource();
    }
}
