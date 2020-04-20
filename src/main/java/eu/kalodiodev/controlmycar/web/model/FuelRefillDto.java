package eu.kalodiodev.controlmycar.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelRefillDto {

    private Long id;
    private LocalDate date;
    private Double odometer;
    private Double volume;
    private Double cost;
    private Boolean fullRefill;
    private String details;
    private String gasStation;
    private Long carId;

}
