package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.stereotype.Service;

@Service
public class JpaCarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public JpaCarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
}
