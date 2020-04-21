package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.web.controllers.CarController;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.services.CarService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
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
    void all_user_cars() throws Exception {
        Set<CarDto> cars = new HashSet<>();
        cars.add(CarDto.builder().id(1L).build());
        cars.add(CarDto.builder().id(2L).build());

        given(carService.allOfUser(anyLong())).willReturn(cars);

        mockMvc.perform(get("/api/v1/users/1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

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

    @Test
    void validate_add_new_car_manufacturer() throws Exception {
        // Manufacturer Required
        CarDto carDto = getValidCarDto();
        carDto.setManufacturer("");
        post_a_new_car(carDto, status().is4xxClientError());

        // Manufacturer Max Length
        carDto = getValidCarDto();
        carDto.setManufacturer(RandomString.make(101));
        post_a_new_car(carDto, status().is4xxClientError());

        // Manufacturer Min Length
        carDto = getValidCarDto();
        carDto.setManufacturer(RandomString.make(2));
        post_a_new_car(carDto, status().is4xxClientError());
    }

    void post_a_new_car(CarDto carDto, ResultMatcher status) throws Exception {
        mockMvc.perform(post("/api/v1/users/1/cars")
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status);
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
