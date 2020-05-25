package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceToServiceDto implements Converter<Service, ServiceDto> {

    @Override
    public ServiceDto convert(Service service) {

        final ServiceDto serviceDto = ServiceDto.builder()
                .id(service.getId())
                .date(service.getDate())
                .title(service.getTitle())
                .description(service.getDescription())
                .odometer(service.getOdometer())
                .cost(service.getCost())
                .build();

        if (service.getCar() != null) {
            serviceDto.setCarId(service.getCar().getId());
        }

        return serviceDto;
    }
}
