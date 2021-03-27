package pl.dkaluza.forum.modules.forum.topic.models.create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreatePostModel {
    @NotBlank(message = "{emptyField}")
    @Size(min = 3, max = 1024, message = "{topic.createPost.invalidContent}")
    private String content;

    @NotNull(message = "{emptyField}")
    private Long topicId;

    @NotNull(message = "{emptyField}")
    private Long authorId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
