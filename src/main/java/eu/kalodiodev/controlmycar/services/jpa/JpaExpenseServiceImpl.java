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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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

    @Override
    public ExpenseDto update(Long userId, Long carId, Long expenseId, ExpenseDto expenseDto) {
        Car car = carRepository.findCarByIdAndUserId(carId, userId)
                .orElseThrow(NotFoundException::new);

        Expense expense = expenseRepository.findByIdAndCarId(expenseId, car.getId())
                .orElseThrow(NotFoundException::new);

        expenseDto.setId(expense.getId());
        expenseDto.setCarId(expense.getId());

        Expense updated = expenseRepository.save(expenseDtoToExpense.convert(expenseDto));

        return expenseToExpenseDto.convert(updated);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long carId, Long expenseId) {
        Optional<Car> carOptional = carRepository.findCarByIdAndUserId(carId, userId);

        if (carOptional.isPresent()) {
            expenseRepository.deleteByIdAndCarId(expenseId, carId);
        }
    }
}
