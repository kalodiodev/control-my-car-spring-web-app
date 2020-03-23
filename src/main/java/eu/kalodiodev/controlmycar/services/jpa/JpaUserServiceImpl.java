package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.UserRepository;
import eu.kalodiodev.controlmycar.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class JpaUserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public JpaUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
