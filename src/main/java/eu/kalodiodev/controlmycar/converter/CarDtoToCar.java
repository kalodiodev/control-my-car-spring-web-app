package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CarDtoToCar implements Converter<CarDto, Car> {

    @Synchronized
    @Override
    public Car convert(CarDto source) {

        final Car car = new Car();
        car.setId(source.getId());
        car.setNumberPlate(source.getNumberPlate());
        car.setManufacturer(source.getManufacturer());
        car.setModel(source.getModel());
        car.setManufacturedYear(source.getManufacturedYear());
        car.setOwnedYear(source.getOwnedYear());
        car.setBoughtPrice(source.getBoughtPrice());
        car.setInitialOdometer(source.getInitialOdometer());

        if (source.getUserId() != null) {
            User user = new User();
            user.setId(source.getUserId());
            car.setUser(user);
            user.getCars().add(car);
        }

        return car;
    }
}
