package eu.kalodiodev.controlmycar.converter;


import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.values.CarValues;
import eu.kalodiodev.controlmycar.domains.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarDtoToCarTest {

    private CarDtoToCar converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new CarDtoToCar();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new CarDto()));
    }

    @Test
    public void convert() throws Exception {
        // given
        CarDto carDto = new CarDto();
        carDto.setId(CarValues.ID_VALUE);
        carDto.setNumberPlate(CarValues.NUMBER_PLATE_VALUE);
        carDto.setManufacturer(CarValues.MANUFACTURER_VALUE);
        carDto.setModel(CarValues.MODEL_VALUE);
        carDto.setManufacturedYear(CarValues.MANUFACTURED_YEAR_VALUE);
        carDto.setOwnedYear(CarValues.OWNED_YEAR_VALUE);
        carDto.setBoughtPrice(CarValues.BOUGHT_PRICE_VALUE);
        carDto.setInitialOdometer(CarValues.INITIAL_ODOMETER_VALUE);
        carDto.setUserId(CarValues.USER_ID_VALUE);

        // When
        Car car = converter.convert(carDto);

        // then
        assertNotNull(car);
        assertEquals(CarValues.ID_VALUE, car.getId());
        assertEquals(CarValues.NUMBER_PLATE_VALUE, car.getNumberPlate());
        assertEquals(CarValues.MANUFACTURER_VALUE, car.getManufacturer());
        assertEquals(CarValues.MODEL_VALUE, car.getModel());
        assertEquals(CarValues.MANUFACTURED_YEAR_VALUE, car.getManufacturedYear());
        assertEquals(CarValues.OWNED_YEAR_VALUE, car.getOwnedYear());
        assertEquals(CarValues.BOUGHT_PRICE_VALUE, car.getBoughtPrice());
        assertEquals(CarValues.INITIAL_ODOMETER_VALUE, car.getInitialOdometer());
        assertNotNull(car.getUser());
    }
}
