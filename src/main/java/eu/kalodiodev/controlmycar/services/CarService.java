package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;

public interface CarService {

    Car save(CarCommand carCommand);

    Car findById(Long id) throws NotFoundException;
}
