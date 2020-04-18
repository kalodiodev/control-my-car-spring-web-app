package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CarToCarDto implements Converter<Car, CarDto> {

    @Synchronized
    @Nullable
    @Override
    public CarDto convert(Car source) {

        if (source == null) {
            return null;
        }

        final CarDto carCommand = new CarDto();
        carCommand.setId(source.getId());
        carCommand.setNumberPlate(source.getNumberPlate());
        carCommand.setManufacturer(source.getManufacturer());
        carCommand.setModel(source.getModel());
        carCommand.setManufacturedYear(source.getManufacturedYear());
        carCommand.setOwnedYear(source.getOwnedYear());
        carCommand.setBoughtPrice(source.getBoughtPrice());
        carCommand.setInitialOdometer(source.getInitialOdometer());

        if (source.getUser() != null) {
            carCommand.setUserId(source.getUser().getId());
        }

        return carCommand;
    }
}