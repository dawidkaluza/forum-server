package pl.dkaluza.forum.modules.forum.topic.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.forum.topic.entities.Topic;

@Repository
public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {
}
