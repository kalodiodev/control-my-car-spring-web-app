package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ServiceToServiceDto;
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

    public JpaServiceServiceImpl(ServiceRepository serviceRepository, CarRepository carRepository, ServiceToServiceDto serviceToServiceDto) {
        this.serviceRepository = serviceRepository;
        this.carRepository = carRepository;
        this.serviceToServiceDto = serviceToServiceDto;
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
}
