package eu.kalodiodev.controlmycar.command;

import eu.kalodiodev.controlmycar.domains.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class CarCommand {

    private Long id;
    private String numberPlate;
    private String manufacturer;
    private String model;
    private Integer manufacturedYear;
    private Integer ownedYear;
    private Double boughtPrice;
    private Double initialOdometer;
    private Long userId;

}
