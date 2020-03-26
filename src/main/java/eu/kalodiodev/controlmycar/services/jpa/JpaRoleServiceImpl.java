package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.repositories.RoleRepository;
import eu.kalodiodev.controlmycar.services.RoleService;
import org.springframework.stereotype.Service;

@Service
public class JpaRoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public JpaRoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
