package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/cars/{carId}/", produces = "application/json")
public class FuelRefillController {

    private final FuelRefillService fuelRefillService;

    public FuelRefillController(FuelRefillService fuelRefillService) {
        this.fuelRefillService = fuelRefillService;
    }

    @GetMapping("fuelrefills")
    public CollectionModel<FuelRefillDto> getAllFuelRefillsOfCar(@AuthenticationPrincipal User user,
                                                                 @PathVariable Long carId) {

        List<FuelRefillDto> fuelRefills = fuelRefillService.findAllByUserIdAndByCarId(user.getId(), carId);

        Link link = linkTo(methodOn(FuelRefillController.class).getAllFuelRefillsOfCar(user, carId)).withSelfRel();

        return new CollectionModel<>(fuelRefills, link);
    }

    @PostMapping("fuelrefills")
    public ResponseEntity<FuelRefillDto> addFuelRefill(@AuthenticationPrincipal User user,
                                                       @PathVariable Long carId,
                                                       @RequestBody @Valid FuelRefillDto fuelRefillDto) {

        return new ResponseEntity<>(fuelRefillService.save(user.getId(), carId, fuelRefillDto), HttpStatus.CREATED);
    }

    @PatchMapping("fuelrefills/{fuelRefillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFuelRefill(@AuthenticationPrincipal User user,
                                 @PathVariable Long carId,
                                 @PathVariable Long fuelRefillId,
                                 @RequestBody @Valid FuelRefillDto fuelRefillDto) {

        fuelRefillService.update(user.getId(), carId, fuelRefillId, fuelRefillDto);
    }

    @DeleteMapping("fuelrefills/{fuelRefillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFuelRefill(@AuthenticationPrincipal User user,
                                 @PathVariable Long carId,
                                 @PathVariable Long fuelRefillId) {

        fuelRefillService.delete(user.getId(), carId, fuelRefillId);
    }
}
