package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByIdAndCarId(Long expenseId, Long carId);
}
