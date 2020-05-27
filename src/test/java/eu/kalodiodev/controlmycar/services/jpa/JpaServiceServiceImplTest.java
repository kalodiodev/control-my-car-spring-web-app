package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ServiceToServiceDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.ServiceRepository;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
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
class JpaServiceServiceImplTest {

    @Mock
    ServiceRepository serviceRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    ServiceToServiceDto serviceToServiceDto;

    @InjectMocks
    JpaServiceServiceImpl serviceService;

    @Test
    void find_all_services_of_car() {
        Car car = new Car();
        car.getServices().add(new Service());
        car.getServices().add(new Service());

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(serviceToServiceDto.convert(any(Service.class))).willReturn(ServiceDto.builder().build());

        assertEquals(2, serviceService.findAllByUserIdAndByCarId(1L, 1L).size());
    }
}