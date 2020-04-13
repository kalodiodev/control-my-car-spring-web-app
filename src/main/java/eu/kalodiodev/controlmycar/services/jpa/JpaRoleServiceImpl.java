package eu.kalodiodev.controlmycar.services.jpa;

import eu.kalodiodev.controlmycar.domains.Role;
import eu.kalodiodev.controlmycar.repositories.RoleRepository;
import eu.kalodiodev.controlmycar.services.RoleService;
import org.springframework.stereotype.Service;

@Service
public class JpaRoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public JpaRoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
