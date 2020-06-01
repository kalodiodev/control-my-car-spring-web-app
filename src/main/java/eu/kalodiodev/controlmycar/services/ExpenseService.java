package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.ExpenseDto;

import java.util.List;

public interface ExpenseService {

    List<ExpenseDto> findAllByUserIdAndByCarId(Long userId, Long carId);
}
