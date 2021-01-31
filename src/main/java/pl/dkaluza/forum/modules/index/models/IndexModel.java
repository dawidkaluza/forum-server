package pl.dkaluza.forum.modules.index.models;

import org.springframework.hateoas.RepresentationModel;

public class IndexModel extends RepresentationModel<IndexModel> {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
