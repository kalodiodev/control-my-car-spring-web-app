package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.ExpenseDto;

import java.util.List;

public interface ExpenseService {

    List<ExpenseDto> findAllByUserIdAndByCarId(Long userId, Long carId);

    ExpenseDto save(Long userId, Long carId, ExpenseDto expenseDto);

    ExpenseDto update(Long userId, Long carId, Long expenseId, ExpenseDto expenseDto);

    void delete(Long userId, Long carId, Long expenseId);
}
