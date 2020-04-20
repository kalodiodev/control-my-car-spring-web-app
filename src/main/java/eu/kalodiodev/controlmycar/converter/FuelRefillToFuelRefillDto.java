package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FuelRefillToFuelRefillDto implements Converter<FuelRefill, FuelRefillDto> {

    @Override
    public FuelRefillDto convert(FuelRefill source) {

        final FuelRefillDto fuelRefillDto = new FuelRefillDto();
        fuelRefillDto.setId(source.getId());
        fuelRefillDto.setDate(source.getDate());
        fuelRefillDto.setOdometer(source.getOdometer());
        fuelRefillDto.setVolume(source.getVolume());
        fuelRefillDto.setCost(source.getCost());
        fuelRefillDto.setFullRefill(source.getFullRefill());
        fuelRefillDto.setDetails(source.getDetails());
        fuelRefillDto.setGasStation(source.getGasStation());

        if (source.getCar() != null) {
            fuelRefillDto.setCarId(source.getCar().getId());
        }

        return fuelRefillDto;
    }
}
