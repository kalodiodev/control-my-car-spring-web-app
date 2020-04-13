package eu.kalodiodev.controlmycar.services;

import eu.kalodiodev.controlmycar.domains.Role;

public interface RoleService {

    Role save(Role role);

    Role findByName(String name);
}
