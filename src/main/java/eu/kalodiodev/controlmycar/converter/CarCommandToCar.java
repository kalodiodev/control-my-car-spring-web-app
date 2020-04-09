package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CarCommandToCar implements Converter<CarCommand, Car> {

    @Override
    public Car convert(CarCommand source) {

        if (source == null) {
            return null;
        }

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
