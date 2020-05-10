package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.services.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("cars")
    public CollectionModel<CarDto> allOfUser(@AuthenticationPrincipal User user) {
        Set<CarDto> cars = carService.allOfUser(user.getId()).stream().map(carDto -> {
            Link selfLink = linkTo(methodOn(CarController.class).findCar(user, carDto.getId())).withSelfRel();
            carDto.add(selfLink);

            return carDto;
        }).collect(Collectors.toSet());

        Link link = linkTo(methodOn(CarController.class).allOfUser(user)).withSelfRel();
        CollectionModel<CarDto> result = new CollectionModel<>(cars, link);

        return result;
    }

    @GetMapping("cars/{carId}")
    public CarDto findCar(@AuthenticationPrincipal User user, @PathVariable Long carId) {
        CarDto carDto = carService.findByUserIdAndCarId(user.getId(), carId);

        Link selfLink = linkTo(methodOn(CarController.class).findCar(user, carId)).withSelfRel();
        carDto.add(selfLink);

        if (carService.allOfUser(user.getId()).size() > 0) {
            Link carsLink = linkTo(methodOn(CarController.class).allOfUser(user)).withRel("all-cars");
            carDto.add(carsLink);
        }

        return carDto;
    }

    @PostMapping("cars")
    public ResponseEntity<CarDto> addCar(@AuthenticationPrincipal User user, @RequestBody @Valid CarDto carDto) {
        CarDto saved = carService.save(user.getId(), carDto);

        Link selfLink = linkTo(methodOn(CarController.class).findCar(user, saved.getId())).withSelfRel();
        saved.add(selfLink);

        Link carsLink = linkTo(methodOn(CarController.class).allOfUser(user)).withRel("all-cars");
        saved.add(carsLink);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@AuthenticationPrincipal User user, @PathVariable Long carId, @RequestBody @Valid CarDto carDto) {

        carService.update(user.getId(), carId, carDto);
    }

    @DeleteMapping("cars/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@AuthenticationPrincipal User user, @PathVariable Long carId) {
        carService.deleteByUserIdAndCarId(user.getId(), carId);
    }
}
