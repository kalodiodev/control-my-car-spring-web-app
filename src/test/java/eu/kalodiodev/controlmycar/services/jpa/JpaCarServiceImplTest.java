package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarCommandToCar;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JpaCarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @Mock
    CarCommandToCar carCommandToCar;

    @InjectMocks
    JpaCarServiceImpl carService;

    private Car car1;

    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setId(1L);
    }

    @Test
    void save_car_command() {
        when(carCommandToCar.convert(any(CarCommand.class))).thenReturn(new Car());
        when(carRepository.save(any(Car.class))).thenReturn(car1);

        assertEquals(car1, carService.save(new CarCommand()));
    }
}