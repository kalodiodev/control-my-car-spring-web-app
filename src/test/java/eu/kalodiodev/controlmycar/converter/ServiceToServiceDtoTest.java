package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.ServiceValues;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceToServiceDtoTest {

    private ServiceToServiceDto converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new ServiceToServiceDto();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Service()));
    }

    @Test
    public void convert() throws Exception {
        // given
        Service service = new Service();
        service.setId(ServiceValues.ID_VALUE);
        service.setTitle(ServiceValues.TITLE_VALUE);
        service.setDescription(ServiceValues.DESCRIPTION_VALUE);
        service.setDate(LocalDate.now());
        service.setOdometer(ServiceValues.ODOMETER_VALUE);
        service.setCost(ServiceValues.COST_VALUE);

        Car car = new Car();
        car.setId(ServiceValues.CAR_ID_VALUE);
        service.setCar(car);

        // when
        ServiceDto serviceDto = converter.convert(service);

        // then
        assertNotNull(serviceDto);
        assertEquals(ServiceValues.ID_VALUE, serviceDto.getId());
        assertEquals(ServiceValues.TITLE_VALUE, serviceDto.getTitle());
        assertEquals(ServiceValues.DESCRIPTION_VALUE, serviceDto.getDescription());
        assertEquals(ServiceValues.ODOMETER_VALUE, serviceDto.getOdometer());
        assertEquals(ServiceValues.COST_VALUE, serviceDto.getCost());
        assertEquals(ServiceValues.CAR_ID_VALUE, serviceDto.getCarId());
    }
}
