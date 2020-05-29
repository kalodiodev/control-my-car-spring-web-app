package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ServiceDtoToService;
import eu.kalodiodev.controlmycar.converter.ServiceToServiceDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.ServiceRepository;
import eu.kalodiodev.controlmycar.services.ServiceService;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final CarRepository carRepository;
    private final ServiceToServiceDto serviceToServiceDto;
    private final ServiceDtoToService serviceDtoToService;

    public JpaServiceServiceImpl(ServiceRepository serviceRepository, CarRepository carRepository, ServiceToServiceDto serviceToServiceDto, ServiceDtoToService serviceDtoToService) {
        this.serviceRepository = serviceRepository;
        this.carRepository = carRepository;
        this.serviceToServiceDto = serviceToServiceDto;
        this.serviceDtoToService = serviceDtoToService;
    }

    @Override
    public List<ServiceDto> findAllByUserIdAndByCarId(Long userId, Long carId) {
        return carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new)
                .getServices()
                .stream()
                .map(serviceToServiceDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDto save(Long userId, Long carId, ServiceDto serviceDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId).orElseThrow(NotFoundException::new);

        serviceDto.setCarId(car.getId());
        eu.kalodiodev.controlmycar.domains.Service service =  serviceRepository.save(serviceDtoToService.convert(serviceDto));

        return serviceToServiceDto.convert(service);
    }

    @Override
    public ServiceDto update(Long userId, Long carId, Long serviceId, ServiceDto serviceDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new);

        eu.kalodiodev.controlmycar.domains.Service service = serviceRepository.findByIdAndCarId(serviceId, car.getId())
                .orElseThrow(NotFoundException::new);

        serviceDto.setId(service.getId());
        serviceDto.setCarId(car.getId());

        eu.kalodiodev.controlmycar.domains.Service updated = serviceRepository.save(serviceDtoToService.convert(serviceDto));

        return serviceToServiceDto.convert(updated);
    }
}
