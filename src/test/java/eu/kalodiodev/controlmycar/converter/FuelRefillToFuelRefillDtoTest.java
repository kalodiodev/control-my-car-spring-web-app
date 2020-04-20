package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.FuelRefillValues;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FuelRefillToFuelRefillDtoTest {

    private FuelRefillToFuelRefillDto converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new FuelRefillToFuelRefillDto();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new FuelRefill()));
    }

    @Test
    public void convert() throws Exception {
        // given
        FuelRefill fuelRefill = new FuelRefill();
        fuelRefill.setId(FuelRefillValues.ID_VALUE);
        fuelRefill.setDate(LocalDate.now());
        fuelRefill.setOdometer(FuelRefillValues.ODOMETER_VALUE);
        fuelRefill.setVolume(FuelRefillValues.VOLUME_VALUE);
        fuelRefill.setCost(FuelRefillValues.COST_VALUE);
        fuelRefill.setFullRefill(FuelRefillValues.FULL_REFILL_VALUE);
        fuelRefill.setDetails(FuelRefillValues.DETAILS_VALUE);
        fuelRefill.setGasStation(FuelRefillValues.GAS_STATION_VALUE);

        Car car = new Car();
        car.setId(FuelRefillValues.CAR_ID_VALUE);
        fuelRefill.setCar(car);

        // when
        FuelRefillDto fuelRefillDto = converter.convert(fuelRefill);

        // then
        assertNotNull(fuelRefillDto);
        assertEquals(FuelRefillValues.ID_VALUE, fuelRefillDto.getId());
        assertEquals(FuelRefillValues.ODOMETER_VALUE, fuelRefillDto.getOdometer());
        assertEquals(FuelRefillValues.VOLUME_VALUE, fuelRefillDto.getVolume());
        assertEquals(FuelRefillValues.COST_VALUE, fuelRefillDto.getCost());
        assertEquals(FuelRefillValues.FULL_REFILL_VALUE, fuelRefillDto.getFullRefill());
        assertEquals(FuelRefillValues.DETAILS_VALUE, fuelRefillDto.getDetails());
        assertEquals(FuelRefillValues.GAS_STATION_VALUE, fuelRefillDto.getGasStation());
        assertEquals(FuelRefillValues.CAR_ID_VALUE, fuelRefillDto.getCarId());
    }
}