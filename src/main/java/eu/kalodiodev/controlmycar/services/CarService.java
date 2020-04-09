package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.domains.Car;

public interface CarService {

    Car save(CarCommand carCommand);
}
