package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.CarDtoToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class JpaCarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarDtoToCar carDtoToCar;

    public JpaCarServiceImpl(CarRepository carRepository, CarDtoToCar carDtoToCar) {
        this.carRepository = carRepository;
        this.carDtoToCar = carDtoToCar;
    }

    @Override
    public Car save(CarDto carDto) {
        return carRepository.save(carDtoToCar.convert(carDto));
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
    public void update(CarDto carDto) {
        findById(carDto.getId());

        carRepository.save(carDtoToCar.convert(carDto));
    }

    @Transactional
    @Override
    public void deleteByUserIdAndCarId(Long userId, Long carId) {
        carRepository.deleteByIdAndUserId(carId, userId);
    }
}
