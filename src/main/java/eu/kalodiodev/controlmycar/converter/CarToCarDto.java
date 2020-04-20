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

        final CarDto carDto = new CarDto();
        carDto.setId(source.getId());
        carDto.setNumberPlate(source.getNumberPlate());
        carDto.setManufacturer(source.getManufacturer());
        carDto.setModel(source.getModel());
        carDto.setManufacturedYear(source.getManufacturedYear());
        carDto.setOwnedYear(source.getOwnedYear());
        carDto.setBoughtPrice(source.getBoughtPrice());
        carDto.setInitialOdometer(source.getInitialOdometer());

        if (source.getUser() != null) {
            carDto.setUserId(source.getUser().getId());
        }

        return carDto;
    }
}