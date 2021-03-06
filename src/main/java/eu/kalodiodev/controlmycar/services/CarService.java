package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;

import java.util.Set;

public interface CarService {

    CarDto save(Long userId, CarDto carDto);

    CarDto findByUserIdAndCarId(Long userId, Long carId);

    Set<CarDto> allOfUser(Long userId);

    CarDto update(Long userId, Long carId, CarDto carDto) throws NotFoundException;

    void deleteByUserIdAndCarId(Long userId, Long carId);
}
