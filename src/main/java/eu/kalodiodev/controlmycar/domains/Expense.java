package eu.kalodiodev.controlmycar.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Expense {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String description;

    @Column
    private Double cost;
}