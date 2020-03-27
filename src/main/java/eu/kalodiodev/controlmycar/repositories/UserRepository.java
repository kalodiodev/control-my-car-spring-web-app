package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
