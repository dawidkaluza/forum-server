package pl.dkaluza.forum.modules.forum.topic.models.create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateTopicModel {
    @NotNull(message = "{emptyField}")
    private Long authorId;

    @NotBlank(message = "{topic.createTopic.invalidTitle}")
    @Size(min = 3, max = 192, message = "{topic.createTopic.invalidTitle}")
    private String title;

    @NotBlank(message = "{topic.createTopic.invalidContent}")
    @Size(min = 3, max = 1024, message = "{topic.createTopic.invalidContent}")
    private String content;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
