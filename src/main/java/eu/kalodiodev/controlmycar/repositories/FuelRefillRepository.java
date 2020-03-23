package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.FuelRefill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelRefillRepository extends JpaRepository<FuelRefill, Long> {
}
