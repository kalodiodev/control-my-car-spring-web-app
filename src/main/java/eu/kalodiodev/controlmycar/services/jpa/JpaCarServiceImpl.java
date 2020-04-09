package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarCommandToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.stereotype.Service;

@Service
public class JpaCarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarCommandToCar carCommandToCar;

    public JpaCarServiceImpl(CarRepository carRepository, CarCommandToCar carCommandToCar) {
        this.carRepository = carRepository;
        this.carCommandToCar = carCommandToCar;
    }

    @Override
    public Car save(CarCommand carCommand) {
        return carRepository.save(carCommandToCar.convert(carCommand));
    }
}
