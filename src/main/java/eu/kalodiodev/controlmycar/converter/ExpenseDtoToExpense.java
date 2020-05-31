package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ExpenseDtoToExpense implements Converter<ExpenseDto, Expense> {

    @Override
    public Expense convert(ExpenseDto expenseDto) {
        final Expense expense = new Expense();
        expense.setId(expenseDto.getId());
        expense.setDate(expenseDto.getDate());
        expense.setTitle(expenseDto.getTitle());
        expense.setDescription(expenseDto.getDescription());
        expense.setCost(expenseDto.getCost());

        if (expenseDto.getCarId() != null) {
            Car car = new Car();
            car.setId(expenseDto.getCarId());

            expense.setCar(car);
            car.getExpenses().add(expense);
        }

        return expense;
    }
}
