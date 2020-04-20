package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
