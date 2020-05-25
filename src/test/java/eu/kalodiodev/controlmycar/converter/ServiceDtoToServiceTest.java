package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.ServiceValues;
import eu.kalodiodev.controlmycar.domains.Service;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceDtoToServiceTest {

    private ServiceDtoToService converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new ServiceDtoToService();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new ServiceDto()));
    }

    @Test
    public void convert() throws Exception {
        // given
        ServiceDto serviceDto = ServiceDto.builder()
                .id(ServiceValues.ID_VALUE)
                .date(LocalDate.now())
                .title(ServiceValues.TITLE_VALUE)
                .description(ServiceValues.DESCRIPTION_VALUE)
                .odometer(ServiceValues.ODOMETER_VALUE)
                .cost(ServiceValues.COST_VALUE)
                .carId(ServiceValues.CAR_ID_VALUE)
                .build();

        // when
        Service service = converter.convert(serviceDto);

        // then
        assertNotNull(service);
        assertEquals(ServiceValues.ID_VALUE, service.getId());
        assertEquals(ServiceValues.TITLE_VALUE, service.getTitle());
        assertEquals(ServiceValues.DESCRIPTION_VALUE, service.getDescription());
        assertEquals(ServiceValues.ODOMETER_VALUE, service.getOdometer());
        assertEquals(ServiceValues.COST_VALUE, service.getCost());
        assertEquals(ServiceValues.CAR_ID_VALUE, service.getCar().getId());
    }
}
