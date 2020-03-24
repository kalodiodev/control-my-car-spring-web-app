package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.ServiceRepository;
import eu.kalodiodev.controlmycar.services.ServiceService;
import org.springframework.stereotype.Service;

@Service
public class JpaServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;

    public JpaServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
}
