package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/{userId}/cars/{carId}/", produces = "application/json")
public class FuelRefillController {

    private final FuelRefillService fuelRefillService;

    public FuelRefillController(FuelRefillService fuelRefillService) {
        this.fuelRefillService = fuelRefillService;
    }

    @GetMapping("fuelrefills")
    public List<FuelRefillDto> getAllFuelRefillsOfCar(@PathVariable Long userId, @PathVariable Long carId) {
        return fuelRefillService.findAllByUserIdAndByCarId(userId, carId);
    }

    @PostMapping("fuelrefills")
    public ResponseEntity<FuelRefillDto> addFuelRefill(@PathVariable Long userId,
                                                   @PathVariable Long carId,
                                                   @RequestBody FuelRefillDto fuelRefillDto) {

        return new ResponseEntity<>(fuelRefillService.save(userId, carId, fuelRefillDto), HttpStatus.CREATED);
    }

    @PatchMapping("fuelrefills/{fuelRefillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFuelRefill(@PathVariable Long userId,
                                 @PathVariable Long carId,
                                 @PathVariable Long fuelRefillId,
                                 @RequestBody FuelRefillDto fuelRefillDto) {

        fuelRefillService.update(userId, carId, fuelRefillId, fuelRefillDto);
    }

    @DeleteMapping("fuelrefills/{fuelRefillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFuelRefill(@PathVariable Long userId,
                                 @PathVariable Long carId,
                                 @PathVariable Long fuelRefillId) {

        fuelRefillService.delete(userId, carId, fuelRefillId);
    }
}
