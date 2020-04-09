package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.values.CarValues;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarToCarCommandTest {

    private CarToCarCommand converter;

    @BeforeEach
    public void setUp()throws Exception {
        converter = new CarToCarCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Car()));
    }

    @Test
    public void convert() throws Exception {
        // given
        Car car = new Car();
        car.setId(CarValues.ID_VALUE);
        car.setNumberPlate(CarValues.NUMBER_PLATE_VALUE);
        car.setManufacturer(CarValues.MANUFACTURER_VALUE);
        car.setModel(CarValues.MODEL_VALUE);
        car.setManufacturedYear(CarValues.MANUFACTURED_YEAR_VALUE);
        car.setOwnedYear(CarValues.OWNED_YEAR_VALUE);
        car.setBoughtPrice(CarValues.BOUGHT_PRICE_VALUE);
        car.setInitialOdometer(CarValues.INITIAL_ODOMETER_VALUE);

        User user = new User();
        user.setId(CarValues.USER_ID_VALUE);
        car.setUser(user);

        // when
        CarCommand command = converter.convert(car);

        // then
        assertNotNull(command);
        assertEquals(CarValues.ID_VALUE, command.getId());
        assertEquals(CarValues.NUMBER_PLATE_VALUE, command.getNumberPlate());
        assertEquals(CarValues.MANUFACTURER_VALUE, command.getManufacturer());
        assertEquals(CarValues.MODEL_VALUE, command.getModel());
        assertEquals(CarValues.MANUFACTURED_YEAR_VALUE, command.getManufacturedYear());
        assertEquals(CarValues.OWNED_YEAR_VALUE, command.getOwnedYear());
        assertEquals(CarValues.BOUGHT_PRICE_VALUE, command.getBoughtPrice());
        assertEquals(CarValues.INITIAL_ODOMETER_VALUE, command.getInitialOdometer());
        assertEquals(CarValues.USER_ID_VALUE, command.getUserId());
    }
}