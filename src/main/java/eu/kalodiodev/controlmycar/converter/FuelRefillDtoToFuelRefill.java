package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class FuelRefillDtoToFuelRefill implements Converter<FuelRefillDto, FuelRefill>
{
        @Synchronized
        @Nullable
        @Override
        public FuelRefill convert(FuelRefillDto source) {

        if (source == null) {
            return null;
        }

        final FuelRefill fuelRefill = new FuelRefill();
        fuelRefill.setId(source.getId());
        fuelRefill.setDate(source.getDate());
        fuelRefill.setOdometer(source.getOdometer());
        fuelRefill.setVolume(source.getVolume());
        fuelRefill.setCost(source.getCost());
        fuelRefill.setFullRefill(source.getFullRefill());
        fuelRefill.setDetails(source.getDetails());
        fuelRefill.setGasStation(source.getGasStation());

        if (source.getCarId() != null) {
            Car car = new Car();
            car.setId(source.getCarId());

            fuelRefill.setCar(car);
            car.getFuelRefills().add(fuelRefill);
        }

        return fuelRefill;
    }
}
