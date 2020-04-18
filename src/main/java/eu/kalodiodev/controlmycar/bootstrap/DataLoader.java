package eu.kalodiodev.controlmycar.bootstrap;

import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.Role;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.CarService;
import eu.kalodiodev.controlmycar.services.RoleService;
import eu.kalodiodev.controlmycar.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final CarService carService;
    private final CarToCarDto carToCarDto;

    public DataLoader(RoleService roleService, UserService userService, CarService carService, CarToCarDto carToCarDto) {
        this.roleService = roleService;
        this.userService = userService;
        this.carService = carService;
        this.carToCarDto = carToCarDto;
    }

    @Override
    public void run(String... args) throws Exception {

        // Add Roles
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleService.save(adminRole);

        Role userRole = new Role();
        userRole.setName("USER");
        roleService.save(userRole);

        // Add Users
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = encoder.encode("password");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword(secret);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.addRole(userRole);

        userService.save(user);

        // Add Cars
        Car car = new Car();
        car.setNumberPlate("AAA-1234");
        car.setManufacturer("Nissan");
        car.setModel("Qashqai");
        car.setManufacturedYear(2009);
        car.setOwnedYear(2009);
        car.setInitialOdometer(10d);
        car.setBoughtPrice(33100d);
        car.setUser(user);

        carService.save(carToCarDto.convert(car));
    }
}
