package pl.dkaluza.forum.modules.user.base.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByCreatedAtBeforeAndEnabledIsFalse(LocalDateTime createdAt);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
