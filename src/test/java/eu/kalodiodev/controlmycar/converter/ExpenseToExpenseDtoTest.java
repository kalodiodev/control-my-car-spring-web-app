package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.ExpenseValues;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseToExpenseDtoTest {

    private ExpenseToExpenseDto converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new ExpenseToExpenseDto();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Expense()));
    }

    @Test
    public void convert() throws Exception {
        // given
        Expense expense = new Expense();
        expense.setId(ExpenseValues.ID_VALUE);
        expense.setTitle(ExpenseValues.TITLE_VALUE);
        expense.setDescription(ExpenseValues.DESCRIPTION_VALUE);
        expense.setDate(LocalDate.now());
        expense.setCost(ExpenseValues.COST_VALUE);

        Car car = new Car();
        car.setId(ExpenseValues.CAR_ID_VALUE);
        expense.setCar(car);

        // when
        ExpenseDto expenseDto = converter.convert(expense);

        // then
        assertNotNull(expenseDto);
        assertEquals(ExpenseValues.ID_VALUE, expenseDto.getId());
        assertEquals(ExpenseValues.TITLE_VALUE, expenseDto.getTitle());
        assertEquals(ExpenseValues.DESCRIPTION_VALUE, expenseDto.getDescription());
        assertEquals(ExpenseValues.COST_VALUE, expenseDto.getCost());
        assertEquals(ExpenseValues.CAR_ID_VALUE, expenseDto.getCarId());
    }
}