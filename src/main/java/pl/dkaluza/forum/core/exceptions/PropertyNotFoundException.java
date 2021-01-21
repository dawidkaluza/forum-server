package pl.dkaluza.forum.core.exceptions;

public class PropertyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7908194860256268539L;

    public PropertyNotFoundException(String name) {
        super("Can't find '" + name + "' property");
    }
}
