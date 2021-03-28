package pl.dkaluza.forum.modules.forum.topic.models.basic;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.ZonedDateTime;

@Relation(itemRelation = "topic", collectionRelation = "topic")
public class TopicModel extends RepresentationModel<TopicModel> {
    private Long id;
    private String title;
    private boolean closed;
    private ZonedDateTime createdAt;
    private Long authorId;
    private PostModel lastPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public PostModel getLastPost() {
        return lastPost;
    }

    public void setLastPost(PostModel lastPost) {
        this.lastPost = lastPost;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
            "id=" + id +
            '}';
    }
}
