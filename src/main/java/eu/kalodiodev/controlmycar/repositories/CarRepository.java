package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
