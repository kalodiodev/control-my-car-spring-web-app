package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.services.RoleService;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.UserRepository;
import eu.kalodiodev.controlmycar.services.UserService;
import eu.kalodiodev.controlmycar.web.model.authentication.RegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public JpaUserServiceImpl(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              RoleService roleService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        user.addRole(roleService.findByName("USER"));

        return save(user);
    }
}
