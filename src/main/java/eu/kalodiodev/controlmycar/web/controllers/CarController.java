package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/", produces = "application/json")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("users/{userId}/cars/{carId}")
    public CarDto findCar(@PathVariable Long userId, @PathVariable Long carId) {
        return carService.findByUserIdAndCarId(userId, carId);
    }

    @PostMapping("users/{userId}/cars")
    public ResponseEntity<CarDto> addCar(@PathVariable Long userId, @RequestBody CarDto carDto) {
        carDto.setUserId(userId);

        return new ResponseEntity<>(carService.save(carDto), HttpStatus.CREATED);
    }

    @PatchMapping("users/{userId}/cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@PathVariable Long userId, @PathVariable Long carId, @RequestBody CarDto carDto) {
        carDto.setUserId(userId);
        carDto.setId(carId);

        carService.update(carDto);
    }

    @DeleteMapping("users/{userId}/cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long userId, @PathVariable Long carId) {
        carService.deleteByUserIdAndCarId(userId, carId);
    }
}
