package pl.dkaluza.forum.modules.user.confirmRegistration.listeners.user.onUserDelete;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
class OnUserDeleteListenerErrorHandler implements ErrorHandler {
    @Override
    public void handleError(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        throw new RuntimeException(throwable);
    }
}
