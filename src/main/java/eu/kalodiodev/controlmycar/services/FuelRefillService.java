package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;

import java.util.List;

public interface FuelRefillService {

    List<FuelRefillDto> findAllByUserIdAndByCarId(Long userId, Long carId);

}
