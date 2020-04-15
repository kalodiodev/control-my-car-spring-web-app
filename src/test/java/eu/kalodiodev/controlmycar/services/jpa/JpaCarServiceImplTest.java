package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarCommandToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JpaCarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @Mock
    CarCommandToCar carCommandToCar;

    @InjectMocks
    JpaCarServiceImpl carService;

    private Car car1;
    private static final Long CAR_ID = 1L;
    private final Set<Car> carSet = new HashSet<>();


    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setId(CAR_ID);

        carSet.add(car1);
    }

    @Test
    void find_all_cars_of_user() {
        when(carRepository.findAllByUserId(anyLong())).thenReturn(carSet);

        Set<Car> cars = carService.allOfUser(1L);

        assertEquals(1, cars.size());
    }

    @Test
    void find_car_by_id() throws NotFoundException {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car1));

        assertEquals(car1, carService.findById(CAR_ID));
    }

    @Test
    void not_found_car() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.findById(CAR_ID));
    }

    @Test
    void save_car_command() {
        when(carCommandToCar.convert(any(CarCommand.class))).thenReturn(new Car());
        when(carRepository.save(any(Car.class))).thenReturn(car1);

        assertEquals(car1, carService.save(new CarCommand()));
    }

    @Test
    void update_car() {
        CarCommand carCommand = new CarCommand();
        carCommand.setId(CAR_ID);
        carCommand.setNumberPlate("AAA-2345");

        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car1));
        when(carCommandToCar.convert(any(CarCommand.class))).thenReturn(car1);

        carService.update(carCommand);

        verify(carRepository, times(1)).save(any(Car.class));
    }
}