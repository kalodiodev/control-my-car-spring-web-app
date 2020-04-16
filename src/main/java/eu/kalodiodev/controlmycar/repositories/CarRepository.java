package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CarRepository extends JpaRepository<Car, Long> {

    Set<Car> findAllByUserId(Long userId);

    Optional<Car> findCarByIdAndUserId(Long carId, Long userId);

    void deleteByIdAndUserId(Long carId, Long userId);
}
