package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.web.model.UserDto;
import eu.kalodiodev.controlmycar.domains.User;

public interface UserService {

    User save(User user);

    User register(UserDto userDto);
}
