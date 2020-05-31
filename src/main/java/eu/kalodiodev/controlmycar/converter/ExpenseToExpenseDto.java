package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ExpenseToExpenseDto implements Converter<Expense, ExpenseDto> {

    @Override
    public ExpenseDto convert(Expense expense) {

        final ExpenseDto expenseDto = ExpenseDto.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .title(expense.getTitle())
                .description(expense.getDescription())
                .cost(expense.getCost())
                .build();

        if (expense.getCar() != null) {
            expenseDto.setCarId(expense.getCar().getId());
        }

        return expenseDto;
    }
}