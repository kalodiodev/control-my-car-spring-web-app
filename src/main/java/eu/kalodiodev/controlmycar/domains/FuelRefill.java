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
public class FuelRefill {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private Double odometer;

    @Column
    private Double volume;

    @Column
    private Double cost;

    @Column
    private Boolean fullRefill;

    @Column
    private String details;

    @Column
    private String gasStation;
}
