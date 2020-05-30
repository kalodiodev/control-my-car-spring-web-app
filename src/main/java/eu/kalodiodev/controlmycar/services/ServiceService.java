package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.ServiceDto;

import java.util.List;

public interface ServiceService {

    List<ServiceDto> findAllByUserIdAndByCarId(Long userId, Long carId);

    ServiceDto save(Long userId, Long carId, ServiceDto serviceDto);

    ServiceDto update(Long userId, Long carId, Long serviceId, ServiceDto serviceDto);

    void delete(Long userId, Long carId, Long serviceId);
}
