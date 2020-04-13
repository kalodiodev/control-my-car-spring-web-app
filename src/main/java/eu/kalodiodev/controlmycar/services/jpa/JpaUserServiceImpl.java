package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.command.UserCommand;
import eu.kalodiodev.controlmycar.converter.UserCommandToUser;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.UserRepository;
import eu.kalodiodev.controlmycar.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCommandToUser userCommandToUser;
    private final PasswordEncoder passwordEncoder;

    public JpaUserServiceImpl(UserRepository userRepository,
                              UserCommandToUser userCommandToUser,
                              PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userCommandToUser = userCommandToUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(UserCommand userCommand) {
        User user = userCommandToUser.convert(userCommand);

        if (user == null) {
            return null;
        }

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        return save(user);
    }
}
