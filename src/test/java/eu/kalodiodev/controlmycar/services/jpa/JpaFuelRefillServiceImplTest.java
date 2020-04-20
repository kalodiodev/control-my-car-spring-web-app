package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.FuelRefillToFuelRefillDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.FuelRefillRepository;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JpaFuelRefillServiceImplTest {

    @Mock
    FuelRefillRepository fuelRefillRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto;

    @InjectMocks
    JpaFuelRefillServiceImpl fuelRefillService;

    @Test
    void find_all_refills_of_car() {
        Car car = new Car();
        car.getFuelRefills().add(new FuelRefill());
        car.getFuelRefills().add(new FuelRefill());

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(fuelRefillToFuelRefillDto.convert(any(FuelRefill.class))).willReturn(FuelRefillDto.builder().build());

        assertEquals(2, fuelRefillService.findAllByUserIdAndByCarId(1L, 1L).size());
    }
}