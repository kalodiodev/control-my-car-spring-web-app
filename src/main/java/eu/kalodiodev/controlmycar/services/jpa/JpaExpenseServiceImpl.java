package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.ExpenseRepository;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import org.springframework.stereotype.Service;

@Service
public class JpaExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;

    public JpaExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
}
