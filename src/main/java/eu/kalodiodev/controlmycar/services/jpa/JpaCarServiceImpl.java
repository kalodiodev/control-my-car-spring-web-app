package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarCommandToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    @Override
    public Car findById(Long id) throws NotFoundException {
        Optional<Car> carOptional = carRepository.findById(id);

        if (carOptional.isEmpty()) {
            throw new NotFoundException("Car not found");
        }

        return carOptional.get();
    }

    @Override
    public Car findByUserIdAndCarId(Long userId, Long carId) {
        Optional<Car> carOptional = carRepository.findCarByIdAndUserId(carId, userId);

        if (carOptional.isEmpty()) {
            throw new NotFoundException("Car not found for given user");
        }

        return carOptional.get();
    }

    @Override
    public Set<Car> allOfUser(Long userId) {
        return carRepository.findAllByUserId(userId);
    }

    @Override
    public void update(CarCommand carCommand) {
        findById(carCommand.getId());

        carRepository.save(carCommandToCar.convert(carCommand));
    }
}
