package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.ServiceDto;

import java.util.List;

public interface ServiceService {

    List<ServiceDto> findAllByUserIdAndByCarId(Long userId, Long carId);

    ServiceDto save(Long id, Long carId, ServiceDto serviceDto);
}
