package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cars/{carId}/", produces = "application/json")
public class FuelRefillController {

    private final FuelRefillService fuelRefillService;

    public FuelRefillController(FuelRefillService fuelRefillService) {
        this.fuelRefillService = fuelRefillService;
    }

    @GetMapping("fuelrefills")
    public List<FuelRefillDto> getAllFuelRefillsOfCar(@AuthenticationPrincipal User user, @PathVariable Long carId) {
        return fuelRefillService.findAllByUserIdAndByCarId(user.getId(), carId);
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
