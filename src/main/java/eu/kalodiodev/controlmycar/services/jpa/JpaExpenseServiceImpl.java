package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.converter.ExpenseDtoToExpense;
import eu.kalodiodev.controlmycar.converter.ExpenseToExpenseDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Expense;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.repositories.CarRepository;
import eu.kalodiodev.controlmycar.repositories.ExpenseRepository;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CarRepository carRepository;
    private final ExpenseToExpenseDto expenseToExpenseDto;
    private final ExpenseDtoToExpense expenseDtoToExpense;

    public JpaExpenseServiceImpl(ExpenseRepository expenseRepository,
                                 CarRepository carRepository,
                                 ExpenseToExpenseDto expenseToExpenseDto,
                                 ExpenseDtoToExpense expenseDtoToExpense) {

        this.expenseRepository = expenseRepository;
        this.carRepository = carRepository;
        this.expenseToExpenseDto = expenseToExpenseDto;
        this.expenseDtoToExpense = expenseDtoToExpense;
    }

    @Override
    public List<ExpenseDto> findAllByUserIdAndByCarId(Long userId, Long carId) {
        return carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new)
                .getExpenses()
                .stream()
                .map(expenseToExpenseDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDto save(Long userId, Long carId, ExpenseDto expenseDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId).orElseThrow(NotFoundException::new);

        expenseDto.setCarId(car.getId());
        Expense expense =  expenseRepository.save(expenseDtoToExpense.convert(expenseDto));

        return expenseToExpenseDto.convert(expense);
    }
}
