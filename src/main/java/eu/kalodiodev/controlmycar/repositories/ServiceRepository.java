package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
