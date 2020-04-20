package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.CarDtoToCar;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JpaCarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @Mock
    CarDtoToCar carDtoToCar;

    @Mock
    CarToCarDto carToCarDto;

    @InjectMocks
    JpaCarServiceImpl carService;

    private Car car1;
    private CarDto carDto;
    private static final Long CAR_ID = 1L;
    private static final Long USER_ID = 1L;
    private final Set<Car> carSet = new HashSet<>();

    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setId(CAR_ID);

        carSet.add(car1);

        carDto = CarDto.builder()
                .id(1L)
                .manufacturer("Toyota")
                .model("Yaris")
                .boughtPrice(7000d)
                .initialOdometer(250d)
                .build();
    }

    @Test
    void find_all_cars_of_user() {
        given(carRepository.findAllByUserId(anyLong())).willReturn(carSet);
        given(carToCarDto.convert(any(Car.class))).willReturn(new CarDto());

        Set<CarDto> cars = carService.allOfUser(1L);

        assertEquals(1, cars.size());
    }

    @Test
    void find_car_by_user_id_and_car_id() {
        given(carRepository.findCarByIdAndUserId(CAR_ID, USER_ID)).willReturn(Optional.of(new Car()));
        given(carToCarDto.convert(any(Car.class))).willReturn(carDto);

        assertEquals(carDto, carService.findByUserIdAndCarId(USER_ID, CAR_ID));
    }

    @Test
    void not_found_car() {
        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.findByUserIdAndCarId(USER_ID, CAR_ID));
    }

    @Test
    void save_car_command() {
        given(carDtoToCar.convert(any(CarDto.class))).willReturn(new Car());
        given(carRepository.save(any(Car.class))).willReturn(new Car());
        given(carToCarDto.convert(any(Car.class))).willReturn(carDto);

        assertEquals(carDto, carService.save(USER_ID, new CarDto()));
    }

    @Test
    void update_car() {
        given(carRepository.findCarByIdAndUserId(CAR_ID, USER_ID)).willReturn(Optional.of(new Car()));
        given(carDtoToCar.convert(any(CarDto.class))).willReturn(new Car());
        given(carRepository.save(any(Car.class))).willReturn(new Car());
        given(carToCarDto.convert(any(Car.class))).willReturn(carDto);

        CarDto updatedCarDto = carService.update(USER_ID, CAR_ID, carDto);

        assertEquals(carDto, updatedCarDto);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void update_car_not_found() {
        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.update(USER_ID, CAR_ID, new CarDto()));
    }

    @Test
    void delete_car_by_id_and_user_id() {
        carService.deleteByUserIdAndCarId(USER_ID, CAR_ID);

        verify(carRepository, times(1)).deleteByIdAndUserId(CAR_ID, USER_ID);
    }
}