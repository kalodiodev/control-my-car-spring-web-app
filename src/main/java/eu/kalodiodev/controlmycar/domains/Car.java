package eu.kalodiodev.controlmycar.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(nullable = true)
    private Integer manufacturedYear;

    @Column(nullable = true)
    private Integer ownedYear;

    @Column
    private Double boughtPrice;

    @Column(nullable = false)
    private Double initialOdometer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
