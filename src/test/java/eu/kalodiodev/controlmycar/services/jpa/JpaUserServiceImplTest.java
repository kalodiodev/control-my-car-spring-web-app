package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.domains.Role;
import eu.kalodiodev.controlmycar.exceptions.UserAlreadyExistsException;
import eu.kalodiodev.controlmycar.services.RoleService;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.UserRepository;
import eu.kalodiodev.controlmycar.web.model.authentication.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JpaUserServiceImpl userService;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");
    }

    @Test
    void save_user() {
        given(userRepository.save(any(User.class))).willReturn(user1);

        assertEquals(user1, userService.save(user1));
    }

    @Test
    void register_user() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("password");

        user1.setPassword("password");

        given(userRepository.save(any(User.class))).willReturn(user1);
        given(roleService.findByName(anyString())).willReturn(new Role());
        given(passwordEncoder.encode(anyString())).willReturn("asdffdsadf");

        assertEquals(user1, userService.register(registrationRequest));
    }

    @Test
    void register_user_that_already_exists() {
        RegistrationRequest registerRequest = new RegistrationRequest();
        registerRequest.setEmail("test@example.com");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> { userService.register(registerRequest); });
    }
}