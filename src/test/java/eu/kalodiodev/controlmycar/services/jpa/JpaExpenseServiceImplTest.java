package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ExpenseDtoToExpense;
import eu.kalodiodev.controlmycar.converter.ExpenseToExpenseDto;

import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
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

    @Mock
    ExpenseDtoToExpense expenseDtoToExpense;

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

    @Test
    void save_expense_command() {
        Car car = new Car();
        car.setId(1L);

        ExpenseDto expenseDto = ExpenseDto.builder().carId(car.getId()).build();

        Expense expense = new Expense();
        expense.setCar(car);

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(car));
        given(expenseDtoToExpense.convert(expenseDto)).willReturn(expense);
        given(expenseRepository.save(expense)).willReturn(expense);
        given(expenseToExpenseDto.convert(expense)).willReturn(expenseDto);

        assertEquals(expenseDto, expenseService.save(1L, 1L, new ExpenseDto()));
    }

    @Test
    void save_expense_to_not_existent_car() {
        ExpenseDto expenseDto = ExpenseDto.builder().build();

        given(carRepository.findCarByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> expenseService.save(1L, 1L, expenseDto));
    }
}