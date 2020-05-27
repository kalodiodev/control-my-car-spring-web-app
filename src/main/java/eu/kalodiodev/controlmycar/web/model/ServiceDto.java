package eu.kalodiodev.controlmycar.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(value = "service", collectionRelation = "services")
public class ServiceDto {

    private Long id;
    private LocalDate date;
    private String title;
    private String description;
    private Double odometer;
    private Double cost;
    private Long carId;
}
