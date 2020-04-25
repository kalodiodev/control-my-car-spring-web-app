package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.FuelRefillDtoToFuelRefill;
import eu.kalodiodev.controlmycar.converter.FuelRefillToFuelRefillDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaFuelRefillServiceImplTest {

    @Mock
    FuelRefillRepository fuelRefillRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    FuelRefillToFuelRefillDto fuelRefillToFuelRefillDto;

    @Mock
    FuelRefillDtoToFuelRefill fuelRefillDtoToFuelRefill;

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

    @Test
    void save_fuel_refill_command()
    {
        Car car = new Car();
        car.setId(1L);

        FuelRefillDto fuelRefillDto = FuelRefillDto.builder().carId(car.getId()).build();

        FuelRefill fuelRefill = new FuelRefill();
        fuelRefill.setCar(car);

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(fuelRefillDtoToFuelRefill.convert(fuelRefillDto)).willReturn(fuelRefill);
        given(fuelRefillRepository.save(fuelRefill)).willReturn(fuelRefill);
        given(fuelRefillToFuelRefillDto.convert(fuelRefill)).willReturn(fuelRefillDto);

        assertEquals(fuelRefillDto, fuelRefillService.save(1L, 1L, new FuelRefillDto()));
    }

    @Test
    void save_fuel_refill_to_not_existent_car()
    {
        FuelRefillDto fuelRefillDto = FuelRefillDto.builder().build();

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fuelRefillService.save(1L,1L, fuelRefillDto));
    }

    @Test
    void update_fuel_refill() {
        FuelRefillDto fuelRefillDto = FuelRefillDto.builder()
                .carId(1L)
                .cost(10d)
                .volume(5d)
                .fullRefill(true)
                .build();

        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(fuelRefillRepository.findByIdAndCarId(1L, 1L)).willReturn(Optional.of(new FuelRefill()));
        given(fuelRefillDtoToFuelRefill.convert(any(FuelRefillDto.class))).willReturn(new FuelRefill());
        given(fuelRefillRepository.save(any(FuelRefill.class))).willReturn(new FuelRefill());
        given(fuelRefillToFuelRefillDto.convert(any(FuelRefill.class))).willReturn(fuelRefillDto);

        FuelRefillDto updatedFuelRefillDto = fuelRefillService.update(1L, 1L, 1L, fuelRefillDto);

        assertEquals(fuelRefillDto, updatedFuelRefillDto);
        verify(fuelRefillRepository, times(1)).save(any(FuelRefill.class));
    }

    @Test
    void update_refill_not_found() {
        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(fuelRefillRepository.findByIdAndCarId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fuelRefillService.update(1L, 1L,1L, new FuelRefillDto()));
    }


    @Test
    void delete_fuel_refill() {
        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));

        fuelRefillService.delete(1L, 1L, 1L);

        verify(fuelRefillRepository, times(1)).deleteByIdAndCarId(1L, 1L);
    }

    @Test
    void delete_fuel_refill_that_does_not_exist_or_belong_to_car()  {
        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.empty());

        fuelRefillService.delete(1L, 1L, 1L);

        verifyNoInteractions(fuelRefillRepository);
    }
}