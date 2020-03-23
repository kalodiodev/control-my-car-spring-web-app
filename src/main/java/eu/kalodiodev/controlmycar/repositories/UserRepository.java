package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
