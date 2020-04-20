package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.FuelRefillToFuelRefillDto;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaFuelRefillServiceImpl implements FuelRefillService {

    private final CarRepository carRepository;
    private final FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto;

    public JpaFuelRefillServiceImpl(CarRepository carRepository,
                                    FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto) {

        this.carRepository = carRepository;
        this.fuelRefillToFuelRefillDto = fuelRefillToFuelRefillDto;
    }

    @Override
    public List<FuelRefillDto> findAllByUserIdAndByCarId(Long userId, Long carId) {
        return carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new)
                .getFuelRefills()
                .stream()
                .map(fuelRefillToFuelRefillDto::convert)
                .collect(Collectors.toList());
    }
}
