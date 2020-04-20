package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.CarDtoToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JpaCarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarDtoToCar carDtoToCar;
    private final CarToCarDto carToCarDto;

    public JpaCarServiceImpl(CarRepository carRepository, CarDtoToCar carDtoToCar, CarToCarDto carToCarDto) {
        this.carRepository = carRepository;
        this.carDtoToCar = carDtoToCar;
        this.carToCarDto = carToCarDto;
    }

    @Override
    public CarDto save(Long userId, CarDto carDto) {
        carDto.setUserId(userId);

        return carToCarDto.convert(carRepository.save(carDtoToCar.convert(carDto)));
    }

    @Override
    public CarDto findByUserIdAndCarId(Long userId, Long carId) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId).orElseThrow(NotFoundException::new);

        return carToCarDto.convert(car);
    }

    @Override
    public Set<CarDto> allOfUser(Long userId) {
        return carRepository.findAllByUserId(userId)
                .stream()
                .map(carToCarDto::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public CarDto update(Long userId, Long carId, CarDto carDto) throws NotFoundException {
        carDto.setId(carId);
        carDto.setUserId(userId);

        findByUserIdAndCarId(userId, carId);

        return carToCarDto.convert(carRepository.save(carDtoToCar.convert(carDto)));
    }

    @Transactional
    @Override
    public void deleteByUserIdAndCarId(Long userId, Long carId) {
        carRepository.deleteByIdAndUserId(carId, userId);
    }
}
