package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.ServiceService;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
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
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("services")
    public CollectionModel<ServiceDto> getAllServicesOfCar(@AuthenticationPrincipal User user,
                                                              @PathVariable Long carId) {

        List<ServiceDto> services = serviceService.findAllByUserIdAndByCarId(user.getId(), carId);

        Link link = linkTo(methodOn(ServiceController.class).getAllServicesOfCar(user, carId)).withSelfRel();

        return new CollectionModel<>(services, link);
    }

    @PostMapping("services")
    public ResponseEntity<ServiceDto> addService(@AuthenticationPrincipal User user,
                                                 @PathVariable Long carId,
                                                 @RequestBody @Valid ServiceDto serviceDto) {
        ServiceDto savedServiceDto = serviceService.save(user.getId(), carId, serviceDto);

        Link servicesLink = linkTo(methodOn(ServiceController.class).getAllServicesOfCar(user, carId))
                .withRel("car-services");

        savedServiceDto.add(servicesLink);

        return new ResponseEntity<>(savedServiceDto, HttpStatus.CREATED);
    }
}
