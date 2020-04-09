package eu.kalodiodev.controlmycar.converter;


import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.values.CarValues;
import eu.kalodiodev.controlmycar.domains.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarCommandToCarTest {

    private CarCommandToCar converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new CarCommandToCar();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new CarCommand()));
    }

    @Test
    public void convert() throws Exception {
        // given
        CarCommand command = new CarCommand();
        command.setId(CarValues.ID_VALUE);
        command.setNumberPlate(CarValues.NUMBER_PLATE_VALUE);
        command.setManufacturer(CarValues.MANUFACTURER_VALUE);
        command.setModel(CarValues.MODEL_VALUE);
        command.setManufacturedYear(CarValues.MANUFACTURED_YEAR_VALUE);
        command.setOwnedYear(CarValues.OWNED_YEAR_VALUE);
        command.setBoughtPrice(CarValues.BOUGHT_PRICE_VALUE);
        command.setInitialOdometer(CarValues.INITIAL_ODOMETER_VALUE);
        command.setUserId(CarValues.USER_ID_VALUE);

        // When
        Car car = converter.convert(command);

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
