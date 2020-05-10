package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.web.model.authentication.RegistrationRequest;

public interface UserService {

    User save(User user);

    User register(RegistrationRequest registrationRequest);
}
