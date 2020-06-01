package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ExpenseToExpenseDto;

import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.ExpenseRepository;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JpaExpenseServiceImplTest {

    @Mock
    ExpenseRepository expenseRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    ExpenseToExpenseDto expenseToExpenseDto;

    @InjectMocks
    JpaExpenseServiceImpl expenseService;

    @Test
    void find_all_expenses_of_car() {
        Car car = new Car();
        car.getExpenses().add(new Expense());
        car.getExpenses().add(new Expense());

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(expenseToExpenseDto.convert(any(Expense.class))).willReturn(ExpenseDto.builder().build());

        assertEquals(2, expenseService.findAllByUserIdAndByCarId(1L, 1L).size());
    }
}