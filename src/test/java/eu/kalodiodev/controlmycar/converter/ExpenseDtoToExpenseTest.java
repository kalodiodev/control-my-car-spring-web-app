package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.converter.values.ExpenseValues;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDtoToExpenseTest {

    private ExpenseDtoToExpense converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new ExpenseDtoToExpense();
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new ExpenseDto()));
    }

    @Test
    public void convert() throws Exception {
        // given
        ExpenseDto expenseDto = ExpenseDto.builder()
                .id(ExpenseValues.ID_VALUE)
                .date(LocalDate.now())
                .title(ExpenseValues.TITLE_VALUE)
                .description(ExpenseValues.DESCRIPTION_VALUE)
                .cost(ExpenseValues.COST_VALUE)
                .carId(ExpenseValues.CAR_ID_VALUE)
                .build();

        // when
        Expense expense = converter.convert(expenseDto);

        // then
        assertNotNull(expense);
        assertEquals(ExpenseValues.ID_VALUE, expense.getId());
        assertEquals(ExpenseValues.TITLE_VALUE, expense.getTitle());
        assertEquals(ExpenseValues.DESCRIPTION_VALUE, expense.getDescription());
        assertEquals(ExpenseValues.COST_VALUE, expense.getCost());
        assertEquals(ExpenseValues.CAR_ID_VALUE, expense.getCar().getId());
    }
}