package pl.dkaluza.forum.modules.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.role.entities.UserRole;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser(User user);

    @Query("select ur from UserRole ur join fetch Role r where ur.user = :user")
    List<UserRole> findAllByUserAndFetchRolesEagerly(@Param("user") User user);
}
