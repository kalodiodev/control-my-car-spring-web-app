package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;

import java.util.Set;

public interface CarService {

    Car save(CarDto carDto);

    Car findById(Long id) throws NotFoundException;

    Set<Car> allOfUser(Long userId);

    void update(CarDto carDto);

    Car findByUserIdAndCarId(Long userId, Long carId);

    void deleteByUserIdAndCarId(Long userId, Long carId);
}
