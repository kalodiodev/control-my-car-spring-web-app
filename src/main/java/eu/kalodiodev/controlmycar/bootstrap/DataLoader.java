package eu.kalodiodev.controlmycar.bootstrap;

import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.domains.FuelRefill;
import eu.kalodiodev.controlmycar.domains.Role;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.repositories.FuelRefillRepository;
import eu.kalodiodev.controlmycar.services.CarService;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.services.RoleService;
import eu.kalodiodev.controlmycar.services.UserService;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final CarService carService;
    private final CarToCarDto carToCarDto;

    @Autowired
    private FuelRefillRepository fuelRefillRepository;

    public DataLoader(RoleService roleService, UserService userService, CarService carService, CarToCarDto carToCarDto, FuelRefillService fuelRefillService) {
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

        CarDto carDto = carService.save(user.getId(), carToCarDto.convert(car));
        car.setId(carDto.getId());

        // Fuel Refills
        FuelRefill fuelRefill = new FuelRefill();
        fuelRefill.setDate(LocalDate.of(2019, 12, 10));
        fuelRefill.setOdometer(1000d);
        fuelRefill.setVolume(23.55);
        fuelRefill.setCost(33.59);
        fuelRefill.setFullRefill(true);
        fuelRefill.setDetails("");
        fuelRefill.setGasStation("BP Gas Station");

        fuelRefill.setCar(car);

        fuelRefillRepository.save(fuelRefill);
    }
}
