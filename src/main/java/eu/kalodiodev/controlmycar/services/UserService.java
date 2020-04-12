package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.command.UserCommand;
import eu.kalodiodev.controlmycar.domains.User;

public interface UserService {

    User register(UserCommand userCommand);
}
