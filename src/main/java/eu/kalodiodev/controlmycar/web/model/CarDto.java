package eu.kalodiodev.controlmycar.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(value = "car", collectionRelation = "cars")
public class CarDto extends RepresentationModel<CarDto> {

    @Null
    private Long id;

    @NotBlank
    @Size(min = 4, max = 15)
    private String numberPlate;

    @NotBlank
    @Size(min = 3, max = 100)
    private String manufacturer;

    @NotBlank
    @Size(min = 3, max = 100)
    private String model;

    @Min(value = 1800)
    @Max(value = 2200)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer manufacturedYear;

    @Min(value = 1800)
    @Max(value = 2200)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer ownedYear;

    @Min(value = 0)
    private Double boughtPrice;

    @NotNull
    @Min(value = 0)
    private Double initialOdometer;

    @Null
    private Long userId;
}
