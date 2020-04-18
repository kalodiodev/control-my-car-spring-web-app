package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.web.controllers.CarController;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.converter.CarToCarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.services.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @MockBean
    CarService carService;

    @MockBean
    CarToCarDto carToCarDto;

    @Autowired
    ObjectMapper om;

    @Autowired
    MockMvc mockMvc;

    @Test
    void find_car() throws Exception {
        Car car = new Car();
        car.setId(1L);

        CarDto carDto = new CarDto();
        carDto.setId(1L);

        given(carService.findByUserIdAndCarId(1L, 1L)).willReturn(car);
        given(carToCarDto.convert(car)).willReturn(carDto);

        mockMvc.perform(get("/users/1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void save_car() throws Exception {

        CarDto carDto = getValidCarDto();

        given(carService.save(any(CarDto.class))).willReturn(new Car());
        given(carToCarDto.convert(any(Car.class))).willReturn(carDto);

        mockMvc.perform(post("/users/1/cars")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is("Micra")));
    }

    @Test
    void update_car() throws Exception {
        CarDto carDto = getValidCarDto();

        mockMvc.perform(patch("/users/1/cars/3")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")));
    }

    @Test
    void delete_car() throws Exception {
        mockMvc.perform(delete("/users/1/cars/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")));

        verify(carService, times(1)).deleteByUserIdAndCarId(1L, 3L);
    }

    CarDto getValidCarDto() {
        return CarDto.builder()
                .manufacturer("Nissan")
                .model("Micra")
                .boughtPrice(10000d)
                .initialOdometer(1000d)
                .manufacturedYear(2009)
                .ownedYear(2010)
                .numberPlate("AAA-1234")
                .build();
    }
}
