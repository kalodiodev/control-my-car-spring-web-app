package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceDtoToService implements Converter<ServiceDto, Service> {

    @Override
    public Service convert(ServiceDto serviceDto) {
        final Service service = new Service();
        service.setId(serviceDto.getId());
        service.setDate(serviceDto.getDate());
        service.setTitle(serviceDto.getTitle());
        service.setDescription(serviceDto.getDescription());
        service.setOdometer(serviceDto.getOdometer());
        service.setCost(serviceDto.getCost());

        if (serviceDto.getCarId() != null) {
            Car car = new Car();
            car.setId(serviceDto.getCarId());

            service.setCar(car);
            car.getServices().add(service);
        }

        return service;
    }
}
