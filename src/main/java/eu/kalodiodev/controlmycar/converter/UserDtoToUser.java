package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.web.model.UserDto;
import eu.kalodiodev.controlmycar.domains.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUser implements Converter<UserDto, User> {

    private final CarDtoToCar carDtoToCar;

    public UserDtoToUser(CarDtoToCar carDtoToCar) {
        this.carDtoToCar = carDtoToCar;
    }

    @Synchronized
    @Nullable
    @Override
    public User convert(@Nullable UserDto source) {
        if (source == null) {
            return null;
        }

        final User user = new User();
        user.setId(source.getId());
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setEmail(source.getEmail());
        user.setPassword(source.getPassword());

        if (source.getCars() != null && source.getCars().size() > 0) {
            source.getCars().forEach(car -> user.getCars().add(carDtoToCar.convert(car)));
        }

        return user;
    }
}
