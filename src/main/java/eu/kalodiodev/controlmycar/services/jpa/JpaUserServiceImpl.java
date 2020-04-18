package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.web.model.UserDto;
import eu.kalodiodev.controlmycar.converter.UserDtoToUser;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.UserRepository;
import eu.kalodiodev.controlmycar.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoToUser userDtoToUser;
    private final PasswordEncoder passwordEncoder;

    public JpaUserServiceImpl(UserRepository userRepository,
                              UserDtoToUser userDtoToUser,
                              PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userDtoToUser = userDtoToUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User register(UserDto userDto) {
        User user = userDtoToUser.convert(userDto);

        if (user == null) {
            return null;
        }

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        return save(user);
    }
}
