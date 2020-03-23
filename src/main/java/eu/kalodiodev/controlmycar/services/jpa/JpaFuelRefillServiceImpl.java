package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.FuelRefillRepository;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import org.springframework.stereotype.Service;

@Service
public class JpaFuelRefillServiceImpl implements FuelRefillService {

    private final FuelRefillRepository fuelRefillRepository;

    public JpaFuelRefillServiceImpl(FuelRefillRepository fuelRefillRepository) {
        this.fuelRefillRepository = fuelRefillRepository;
    }
}
