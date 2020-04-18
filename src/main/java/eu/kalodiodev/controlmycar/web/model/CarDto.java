package eu.kalodiodev.controlmycar.web.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {

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
