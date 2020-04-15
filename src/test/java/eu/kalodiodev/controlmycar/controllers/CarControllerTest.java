package eu.kalodiodev.controlmycar.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {

    @Mock
    CarService carService;

    @Mock
    CarToCarCommand carToCarCommand;

    @InjectMocks
    CarController controller;

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
}
