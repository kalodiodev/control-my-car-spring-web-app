package eu.kalodiodev.controlmycar.controllers;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarToCarCommand;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class CarController {

    private final CarService carService;
    private final CarToCarCommand carToCarCommand;

    public CarController(CarService carService, CarToCarCommand carToCarCommand) {
        this.carService = carService;
        this.carToCarCommand = carToCarCommand;
    }

    @GetMapping("/users/{userId}/cars/{carId}")
    public CarCommand findCar(@PathVariable Long userId, @PathVariable Long carId) {
        return carToCarCommand.convert(carService.findByUserIdAndCarId(userId, carId));
    }

    @PostMapping("/users/{userId}/cars")
    public ResponseEntity<CarCommand> addCar(@PathVariable Long userId, @RequestBody CarCommand command) {
        command.setUserId(userId);

        return new ResponseEntity<>(carToCarCommand.convert(carService.save(command)), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/users/{userId}/cars/{carId}")
    public HashMap<String, String> updateCar(@PathVariable Long userId, @PathVariable Long carId, @RequestBody CarCommand command) {
        command.setUserId(userId);
        command.setId(carId);

        carService.update(command);

        HashMap<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car updated successfully");

        return response;
    }
}
