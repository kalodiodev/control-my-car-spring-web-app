package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.values.CarValues;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarToCarDtoTest {

    private CarToCarDto converter;

    @BeforeEach
    public void setUp()throws Exception {
        converter = new CarToCarDto();
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
        CarDto carDto = converter.convert(car);

        // then
        assertNotNull(carDto);
        assertEquals(CarValues.ID_VALUE, carDto.getId());
        assertEquals(CarValues.NUMBER_PLATE_VALUE, carDto.getNumberPlate());
        assertEquals(CarValues.MANUFACTURER_VALUE, carDto.getManufacturer());
        assertEquals(CarValues.MODEL_VALUE, carDto.getModel());
        assertEquals(CarValues.MANUFACTURED_YEAR_VALUE, carDto.getManufacturedYear());
        assertEquals(CarValues.OWNED_YEAR_VALUE, carDto.getOwnedYear());
        assertEquals(CarValues.BOUGHT_PRICE_VALUE, carDto.getBoughtPrice());
        assertEquals(CarValues.INITIAL_ODOMETER_VALUE, carDto.getInitialOdometer());
        assertEquals(CarValues.USER_ID_VALUE, carDto.getUserId());
    }
}