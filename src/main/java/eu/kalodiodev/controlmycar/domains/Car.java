package eu.kalodiodev.controlmycar.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
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

}
