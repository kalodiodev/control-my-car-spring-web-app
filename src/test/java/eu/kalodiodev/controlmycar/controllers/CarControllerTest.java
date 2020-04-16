package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.converter.CarToCarCommand;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

    @Mock
    CarService carService;

    @Mock
    CarToCarCommand carToCarCommand;

    @InjectMocks
    CarController controller;

    private static final ObjectMapper om = new ObjectMapper();

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void find_car() throws Exception {
        Car car = new Car();
        car.setId(1L);

        CarCommand carCommand = new CarCommand();
        carCommand.setId(1L);

        when(carService.findByUserIdAndCarId(1L, 1L)).thenReturn(car);
        when(carToCarCommand.convert(car)).thenReturn(carCommand);

        mockMvc.perform(get("/users/1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void save_car() throws Exception {

        CarCommand carCommand = new CarCommand();
        carCommand.setManufacturer("Nissan");
        carCommand.setModel("Micra");
        carCommand.setBoughtPrice(10000d);
        carCommand.setInitialOdometer(1000d);
        carCommand.setManufacturedYear(2009);
        carCommand.setOwnedYear(2010);
        carCommand.setNumberPlate("AAA-1234");

        when(carService.save(any(CarCommand.class))).thenReturn(new Car());
        when(carToCarCommand.convert(any(Car.class))).thenReturn(carCommand);

        mockMvc.perform(post("/users/1/cars")
                .content(om.writeValueAsString(carCommand))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is("Micra")));
    }
}
