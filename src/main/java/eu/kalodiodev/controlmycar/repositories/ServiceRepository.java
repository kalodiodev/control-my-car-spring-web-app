package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByIdAndCarId(Long serviceId, Long carId);
}
