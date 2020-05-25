package eu.kalodiodev.controlmycar.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Service {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Double odometer;

    @Column(nullable = false)
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
