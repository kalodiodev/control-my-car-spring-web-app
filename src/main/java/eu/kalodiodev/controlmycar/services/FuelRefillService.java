package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;

import java.util.List;

public interface FuelRefillService {

    List<FuelRefillDto> findAllByUserIdAndByCarId(Long userId, Long carId);

    FuelRefillDto save(Long userId, Long carId, FuelRefillDto fuelRefillDto);

    void delete(Long userId, Long carId, Long refillId);

    FuelRefillDto update(Long userId, Long carId, Long fuelRefillId, FuelRefillDto fuelRefillDto);
}
