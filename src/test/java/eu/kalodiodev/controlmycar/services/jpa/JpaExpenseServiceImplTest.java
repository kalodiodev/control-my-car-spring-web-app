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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    void update_expense() {
        ExpenseDto expenseDto = ExpenseDto.builder()
                .carId(1L)
                .cost(10d)
                .build();

        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(expenseRepository.findByIdAndCarId(1L, 1L)).willReturn(Optional.of(new Expense()));
        given(expenseDtoToExpense.convert(any(ExpenseDto.class))).willReturn(new Expense());
        given(expenseRepository.save(any(Expense.class))).willReturn(new Expense());
        given(expenseToExpenseDto.convert(any(Expense.class))).willReturn(expenseDto);

        ExpenseDto updatedExpenseDto = expenseService.update(1L, 1L, 1L, expenseDto);

        assertEquals(expenseDto, updatedExpenseDto);
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void update_expense_not_found() {
        Car car = new Car();
        car.setId(1L);

        given(carRepository.findCarByIdAndUserId(1L, 1L)).willReturn(Optional.of(car));
        given(expenseRepository.findByIdAndCarId(anyLong(), anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> expenseService.update(1L, 1L, 1L, new ExpenseDto()));
    }
}