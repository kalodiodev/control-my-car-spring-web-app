package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.RoleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private JpaRoleServiceImpl roleService;
}