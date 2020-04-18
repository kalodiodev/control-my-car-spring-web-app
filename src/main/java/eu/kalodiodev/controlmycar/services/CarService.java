package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;

import java.util.Set;

public interface CarService {

    CarDto save(CarDto carDto);

    CarDto findById(Long id) throws NotFoundException;

    Set<CarDto> allOfUser(Long userId);

    CarDto update(CarDto carDto);

    CarDto findByUserIdAndCarId(Long userId, Long carId);

    void deleteByUserIdAndCarId(Long userId, Long carId);
}
