package pl.dkaluza.forum.modules.role.models;

import org.springframework.hateoas.RepresentationModel;

public class RoleModel extends RepresentationModel<RoleModel> {
    private Long id;
    private String name;
    private String style;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
