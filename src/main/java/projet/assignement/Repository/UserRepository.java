package projet.assignement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.assignement.Entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}

