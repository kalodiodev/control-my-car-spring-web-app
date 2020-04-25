package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.FuelRefillDtoToFuelRefill;
import eu.kalodiodev.controlmycar.converter.FuelRefillToFuelRefillDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.FuelRefillRepository;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaFuelRefillServiceImpl implements FuelRefillService {

    private final CarRepository carRepository;
    private final FuelRefillRepository fuelRefillRepository;
    private final FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto;
    private final FuelRefillDtoToFuelRefill fuelRefillDtoToFuelRefill;

    public JpaFuelRefillServiceImpl(CarRepository carRepository,
                                    FuelRefillRepository fuelRefillRepository,
                                    FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto,
                                    FuelRefillDtoToFuelRefill fuelRefillDtoToFuelRefill) {

        this.carRepository = carRepository;
        this.fuelRefillRepository = fuelRefillRepository;
        this.fuelRefillToFuelRefillDto = fuelRefillToFuelRefillDto;
        this.fuelRefillDtoToFuelRefill = fuelRefillDtoToFuelRefill;
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

    @Override
    public FuelRefillDto save(Long userId, Long carId, FuelRefillDto fuelRefillDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId).orElseThrow(NotFoundException::new);

        fuelRefillDto.setCarId(car.getId());
        FuelRefill fuelRefill = fuelRefillRepository.save(fuelRefillDtoToFuelRefill.convert(fuelRefillDto));

        return fuelRefillToFuelRefillDto.convert(fuelRefill);
    }

    @Override
    public FuelRefillDto update(Long userId, Long carId, Long fuelRefillId, FuelRefillDto fuelRefillDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new);

        FuelRefill fuelRefill = fuelRefillRepository.findByIdAndCarId(fuelRefillId, car.getId())
                .orElseThrow(NotFoundException::new);

        fuelRefillDto.setId(fuelRefill.getId());
        fuelRefillDto.setCarId(car.getId());

        FuelRefill updated = fuelRefillRepository.save(fuelRefillDtoToFuelRefill.convert(fuelRefillDto));

        return fuelRefillToFuelRefillDto.convert(updated);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long carId, Long refillId) {
        Optional<Car> carOptional = carRepository.findCarByIdAndUserId(carId, userId);

        if (carOptional.isPresent()) {
            fuelRefillRepository.deleteByIdAndCarId(refillId, carId);
        }
    }
}
