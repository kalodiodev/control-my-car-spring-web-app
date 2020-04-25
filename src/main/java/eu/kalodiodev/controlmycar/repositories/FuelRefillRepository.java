package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.FuelRefill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuelRefillRepository extends JpaRepository<FuelRefill, Long> {

    void deleteByIdAndCarId(Long refillId, Long carId);

    Optional<FuelRefill> findByIdAndCarId(Long fuelRefillId, Long carId);
}
