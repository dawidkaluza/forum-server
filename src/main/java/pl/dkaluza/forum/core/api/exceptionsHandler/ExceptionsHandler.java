package pl.dkaluza.forum.core.api.exceptionsHandler;

import org.springframework.core.Ordered;

public interface ExceptionsHandler extends Ordered {
    default ExceptionsHandlerOrder getHandlerOrder() {
        return ExceptionsHandlerOrder.DEFAULT;
    }

    @Override
    default  int getOrder() {
        return getHandlerOrder().ordinal();
    }
}
