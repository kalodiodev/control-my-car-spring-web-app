package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("users/{userId}/cars")
    public CollectionModel<CarDto> allOfUser(@PathVariable Long userId) {
        Set<CarDto> cars = carService.allOfUser(userId).stream().map(carDto -> {
            Link selfLink = linkTo(methodOn(CarController.class).findCar(userId, carDto.getId())).withSelfRel();
            carDto.add(selfLink);

            return carDto;
        }).collect(Collectors.toSet());

        Link link = linkTo(methodOn(CarController.class).allOfUser(userId)).withSelfRel();
        CollectionModel<CarDto> result = new CollectionModel<>(cars, link);

        return result;
    }

    @GetMapping("users/{userId}/cars/{carId}")
    public CarDto findCar(@PathVariable Long userId, @PathVariable Long carId) {
        CarDto carDto = carService.findByUserIdAndCarId(userId, carId);

        Link selfLink = linkTo(methodOn(CarController.class).findCar(userId, carId)).withSelfRel();
        carDto.add(selfLink);

        if (carService.allOfUser(userId).size() > 0) {
            Link carsLink = linkTo(methodOn(CarController.class).allOfUser(userId)).withRel("all-cars");
            carDto.add(carsLink);
        }

        return carDto;
    }

    @PostMapping("users/{userId}/cars")
    public ResponseEntity<CarDto> addCar(@PathVariable Long userId, @RequestBody @Validated CarDto carDto) {
        CarDto saved = carService.save(userId, carDto);

        Link selfLink = linkTo(methodOn(CarController.class).findCar(userId, saved.getId())).withSelfRel();
        saved.add(selfLink);

        Link carsLink = linkTo(methodOn(CarController.class).allOfUser(userId)).withRel("all-cars");
        saved.add(carsLink);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("users/{userId}/cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@PathVariable Long userId, @PathVariable Long carId, @RequestBody @Validated CarDto carDto) {

        carService.update(userId, carId, carDto);
    }

    @DeleteMapping("users/{userId}/cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long userId, @PathVariable Long carId) {
        carService.deleteByUserIdAndCarId(userId, carId);
    }
}
