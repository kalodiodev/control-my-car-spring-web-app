package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;

import java.util.Set;

public interface CarService {

    Car save(CarCommand carCommand);

    Car findById(Long id) throws NotFoundException;

    Set<Car> allOfUser(Long userId);

    void update(CarCommand carCommand);
}
