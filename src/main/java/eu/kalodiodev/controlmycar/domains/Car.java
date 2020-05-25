package eu.kalodiodev.controlmycar.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String numberPlate;

    @Column(nullable = false, columnDefinition = "varchar(110)")
    private String manufacturer;

    @Column(nullable = false, columnDefinition = "varchar(110)")
    private String model;

    @Column(columnDefinition = "integer default 0")
    private Integer manufacturedYear;

    @Column(columnDefinition = "integer default 0")
    private Integer ownedYear;

    @Column(columnDefinition = "decimal(10,2) default 0.0")
    private Double boughtPrice;

    @Column(columnDefinition = "decimal(10,2)")
    private Double initialOdometer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private List<FuelRefill> fuelRefills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private List<Service> services = new ArrayList<>();
}
