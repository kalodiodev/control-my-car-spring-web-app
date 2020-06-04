package eu.kalodiodev.controlmycar.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(value = "service", collectionRelation = "services")
public class ServiceDto extends RepresentationModel<ServiceDto> {

    private Long id;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    @Size(min = 3, max = 190)
    private String title;

    private String description;

    @NotNull
    private Double odometer;

    @NotNull
    @Min(value = 0)
    private Double cost;

    private Long carId;
}
