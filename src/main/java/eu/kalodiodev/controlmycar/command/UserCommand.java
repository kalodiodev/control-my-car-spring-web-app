package eu.kalodiodev.controlmycar.command;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserCommand {

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
