package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ServiceDtoToService;
import eu.kalodiodev.controlmycar.converter.ServiceToServiceDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JpaServiceServiceImplTest {

    @Mock
    ServiceRepository serviceRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    ServiceToServiceDto serviceToServiceDto;

    @Mock
    ServiceDtoToService serviceDtoToService;

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

    @Test
    void save_service_command() {
        Car car = new Car();
        car.setId(1L);

        ServiceDto serviceDto = ServiceDto.builder().carId(car.getId()).build();

        Service service = new Service();
        service.setCar(car);

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(serviceDtoToService.convert(serviceDto)).willReturn(service);
        given(serviceRepository.save(service)).willReturn(service);
        given(serviceToServiceDto.convert(service)).willReturn(serviceDto);

        assertEquals(serviceDto, serviceService.save(1L, 1L, new ServiceDto()));
    }

    @Test
    void save_service_to_not_existent_car() {
        ServiceDto serviceDto = ServiceDto.builder().build();

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> serviceService.save(1L, 1L, serviceDto));
    }

    @Test
    void update_service() {
        ServiceDto serviceDto = ServiceDto.builder()
                .carId(1L)
                .cost(10d)
                .build();

        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(serviceRepository.findByIdAndCarId(1L, 1L)).willReturn(Optional.of(new Service()));
        given(serviceDtoToService.convert(any(ServiceDto.class))).willReturn(new Service());
        given(serviceRepository.save(any(Service.class))).willReturn(new Service());
        given(serviceToServiceDto.convert(any(Service.class))).willReturn(serviceDto);

        ServiceDto updatedServiceDto = serviceService.update(1L, 1L, 1L, serviceDto);

        assertEquals(serviceDto, updatedServiceDto);
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    void update_service_not_found() {
        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(serviceRepository.findByIdAndCarId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> serviceService.update(1L, 1L, 1L, new ServiceDto()));
    }
}