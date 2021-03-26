package pl.dkaluza.forum.modules.forum.topic.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.forum.topic.entities.Post;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;

import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findFirstByTopicOrderByIdDesc(Topic topic);

    Page<Post> findAllByTopicOrderById(Topic topic, Pageable pageable);
}
