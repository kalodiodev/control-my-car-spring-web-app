package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.web.model.UserDto;
import eu.kalodiodev.controlmycar.converter.UserDtoToUser;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoToUser userDtoToUser;

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
        when(userRepository.save(any(User.class))).thenReturn(user1);

        assertEquals(user1, userService.save(user1));
    }

    @Test
    void register_user_command() {
        user1.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userDtoToUser.convert(any(UserDto.class))).thenReturn(user1);
        when(passwordEncoder.encode(anyString())).thenReturn("asdffdsadf");

        assertEquals(user1, userService.register(new UserDto()));
    }
}