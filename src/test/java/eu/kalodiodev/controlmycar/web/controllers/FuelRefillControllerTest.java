package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuelRefillController.class)
@Import(SecurityTestConfig.class)
class FuelRefillControllerTest extends BaseControllerTest {

    @MockBean
    FuelRefillService fuelRefillService;

    User authenticatedUser;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail("test@example.com");
        authenticatedUser.setFirstName("John");
        authenticatedUser.setLastName("Doe");
    }

    @Test
    void all_car_fuel_refills() throws Exception {
        List<FuelRefillDto> refills = new ArrayList<>();
        refills.add(FuelRefillDto.builder().id(1L).build());
        refills.add(FuelRefillDto.builder().id(2L).build());

        given(fuelRefillService.findAllByUserIdAndByCarId(anyLong(), anyLong())).willReturn(refills);

        mockMvc.perform(get("/api/v1/cars/1/fuelrefills").with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.fuelRefills", hasSize(2)))
                .andExpect(jsonPath("$._embedded.fuelRefills[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.fuelRefills[1].id", is(2)));
    }

    @Test
    void save_fuel_refill() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();

        given(fuelRefillService.save(1L, 1L, fuelRefillDto)).willReturn(fuelRefillDto);

        mockMvc.perform(post("/api/v1/cars/1/fuelrefills").with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(fuelRefillDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost", is(20.0)))
                .andExpect(jsonPath("$.volume", is(15.0)));
    }

    @Test
    void update_fuel_refill() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();

        given(fuelRefillService.update(1L, 1L, 1L, fuelRefillDto)).willReturn(fuelRefillDto);

        mockMvc.perform(patch("/api/v1/cars/1/fuelrefills/1")
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(fuelRefillDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_fuel_refill_not_found() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();

        given(fuelRefillService.update(1L, 1L, 1L, fuelRefillDto))
                .willThrow(NotFoundException.class);

        mockMvc.perform(patch("/api/v1/cars/1/fuelrefills/1")
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(fuelRefillDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_fuel_refill() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/1/fuelrefills/1")
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isNoContent());

        verify(fuelRefillService, times(1)).delete(1L, 1L, 1L);
    }

    private FuelRefillDto getValidFuelRefillDto() {
        return FuelRefillDto.builder()
                .cost(20d)
                .volume(15d)
                .odometer(14054d)
                .gasStation("Test Station")
                .fullRefill(true)
                .details("Nothing")
                .date(LocalDate.now())
                .build();
    }
}