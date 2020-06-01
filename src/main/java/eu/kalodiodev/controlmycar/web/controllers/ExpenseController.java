package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/cars/{carId}/", produces = "application/json")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("expenses")
    public CollectionModel<ExpenseDto> getAllExpensesOfCar(@AuthenticationPrincipal User user,
                                                           @PathVariable Long carId) {

        List<ExpenseDto> expenses = expenseService.findAllByUserIdAndByCarId(user.getId(), carId);

        Link link = linkTo(methodOn(ExpenseController.class).getAllExpensesOfCar(user, carId)).withSelfRel();

        return new CollectionModel<>(expenses, link);
    }
}
