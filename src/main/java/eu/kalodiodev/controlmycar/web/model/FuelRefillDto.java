package eu.kalodiodev.controlmycar.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(value = "fuelRefill", collectionRelation = "fuelRefills")
public class FuelRefillDto extends RepresentationModel<FuelRefillDto> {

    @Null
    private Long id;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    private Double odometer;

    @NotNull
    private Double volume;

    @NotNull
    private Double cost;

    @NotNull
    private Boolean fullRefill;

    @Size(max = 250)
    private String details;

    @Size(max = 250)
    private String gasStation;

    @Null
    private Long carId;
}
