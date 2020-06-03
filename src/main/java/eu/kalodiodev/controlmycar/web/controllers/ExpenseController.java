package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
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

    @PostMapping("expenses")
    public ResponseEntity<ExpenseDto> addExpense(@AuthenticationPrincipal User user,
                                                 @PathVariable Long carId,
                                                 @RequestBody @Valid ExpenseDto expenseDto) {
        ExpenseDto savedExpenseDto = expenseService.save(user.getId(), carId, expenseDto);

        Link expensesLink = linkTo(methodOn(ExpenseController.class).getAllExpensesOfCar(user, carId))
                .withRel("car-expenses");

        savedExpenseDto.add(expensesLink);

        return new ResponseEntity<>(savedExpenseDto, HttpStatus.CREATED);
    }

    @PatchMapping("expenses/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateExpense(@AuthenticationPrincipal User user,
                              @PathVariable Long carId,
                              @PathVariable Long expenseId,
                              @RequestBody @Valid ExpenseDto expenseDto) {

        expenseService.update(user.getId(), carId, expenseId, expenseDto);
    }
}
