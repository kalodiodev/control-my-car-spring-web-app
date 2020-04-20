package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.FuelRefillValues;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FuelRefillDtoToFuelRefillTest {

    private FuelRefillDtoToFuelRefill converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new FuelRefillDtoToFuelRefill();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new FuelRefillDto()));
    }

    @Test
    public void convert() throws Exception {
        // given
        FuelRefillDto fuelRefillDto = FuelRefillDto.builder()
                .id(FuelRefillValues.ID_VALUE)
                .date(LocalDate.now())
                .odometer(FuelRefillValues.ODOMETER_VALUE)
                .volume(FuelRefillValues.VOLUME_VALUE)
                .cost(FuelRefillValues.COST_VALUE)
                .details(FuelRefillValues.DETAILS_VALUE)
                .gasStation(FuelRefillValues.GAS_STATION_VALUE)
                .carId(FuelRefillValues.CAR_ID_VALUE)
                .build();

        // when
        FuelRefill fuelRefill = converter.convert(fuelRefillDto);

        // then
        assertNotNull(fuelRefill);
        assertEquals(FuelRefillValues.ID_VALUE, fuelRefill.getId());
        assertEquals(FuelRefillValues.ODOMETER_VALUE, fuelRefill.getOdometer());
        assertEquals(FuelRefillValues.VOLUME_VALUE, fuelRefill.getVolume());
        assertEquals(FuelRefillValues.COST_VALUE, fuelRefill.getCost());
        assertEquals(FuelRefillValues.DETAILS_VALUE, fuelRefill.getDetails());
        assertEquals(FuelRefillValues.GAS_STATION_VALUE, fuelRefill.getGasStation());
        assertEquals(FuelRefillValues.CAR_ID_VALUE, fuelRefill.getCar().getId());
    }
}