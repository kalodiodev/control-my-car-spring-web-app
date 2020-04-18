package eu.kalodiodev.controlmycar.web.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Set<CarDto> cars = new HashSet<>();

    @NotNull
    @Size(min = 8, max = 100)
    protected String password;

    @NotNull
    @Size(min = 8, max = 100)
    protected String passwordConfirm;
}
