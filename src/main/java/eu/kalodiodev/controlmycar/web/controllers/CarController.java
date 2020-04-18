package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class CarController {

    private final CarService carService;
    private final CarToCarDto carToCarCommand;

    public CarController(CarService carService, CarToCarDto carToCarCommand) {
        this.carService = carService;
        this.carToCarCommand = carToCarCommand;
    }

    @GetMapping("/users/{userId}/cars/{carId}")
    public CarDto findCar(@PathVariable Long userId, @PathVariable Long carId) {
        return carToCarCommand.convert(carService.findByUserIdAndCarId(userId, carId));
    }

    @PostMapping("/users/{userId}/cars")
    public ResponseEntity<CarDto> addCar(@PathVariable Long userId, @RequestBody CarDto command) {
        command.setUserId(userId);

        return new ResponseEntity<>(carToCarCommand.convert(carService.save(command)), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/users/{userId}/cars/{carId}")
    public HashMap<String, String> updateCar(@PathVariable Long userId, @PathVariable Long carId, @RequestBody CarDto command) {
        command.setUserId(userId);
        command.setId(carId);

        carService.update(command);

        HashMap<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car updated successfully");

        return response;
    }

    @DeleteMapping(value = "/users/{userId}/cars/{carId}")
    public HashMap<String, String> deleteCar(@PathVariable Long userId, @PathVariable Long carId) {
        carService.deleteByUserIdAndCarId(userId, carId);

        HashMap<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car deleted successfully");

        return response;
    }
}
