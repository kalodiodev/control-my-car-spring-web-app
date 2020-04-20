package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.web.controllers.CarController;
import eu.kalodiodev.controlmycar.web.model.CarDto;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @MockBean
    CarService carService;

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

        given(carService.findByUserIdAndCarId(1L, 1L)).willReturn(carDto);

        mockMvc.perform(get("/api/v1/users/1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void find_car_not_found() throws Exception {
        given(carService.findByUserIdAndCarId(1L, 1L)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/users/1/cars/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_car() throws Exception {

        CarDto carDto = getValidCarDto();

        given(carService.save(1L, carDto)).willReturn(carDto);

        mockMvc.perform(post("/api/v1/users/1/cars")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is("Micra")));
    }

    @Test
    void update_car() throws Exception {
        CarDto carDto = getValidCarDto();

        mockMvc.perform(patch("/api/v1/users/1/cars/3")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_car_not_found() throws Exception {
        given(carService.update(anyLong(), anyLong(), any(CarDto.class))).willThrow(NotFoundException.class);

        CarDto carDto = getValidCarDto();

        mockMvc.perform(patch("/api/v1/users/1/cars/3")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_car() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1/cars/3"))
                .andExpect(status().isNoContent());

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
