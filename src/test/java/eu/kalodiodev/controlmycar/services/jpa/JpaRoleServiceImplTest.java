package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.domains.Role;
import eu.kalodiodev.controlmycar.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaRoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private JpaRoleServiceImpl roleService;

    @Test
    void save_role() {
        Role role = new Role();
        role.setId(1L);

        when(roleRepository.save(role)).thenReturn(role);

        assertEquals(role, roleService.save(role));
    }

}