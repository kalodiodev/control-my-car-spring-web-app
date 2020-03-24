package eu.kalodiodev.controlmycar.repositories;

import eu.kalodiodev.controlmycar.domains.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
