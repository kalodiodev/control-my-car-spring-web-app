package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.controllers.FuelRefillController;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuelRefillController.class)
class FuelRefillControllerTest {

    @MockBean
    FuelRefillService fuelRefillService;

    @Autowired
    ObjectMapper om;

    @Autowired
    MockMvc mockMvc;

    @Test
    void all_car_fuel_refills() throws Exception {
        List<FuelRefillDto> refills = new ArrayList<>();
        refills.add(FuelRefillDto.builder().id(1L).build());
        refills.add(FuelRefillDto.builder().id(2L).build());

        given(fuelRefillService.findAllByUserIdAndByCarId(anyLong(), anyLong())).willReturn(refills);

        mockMvc.perform(get("/api/v1/users/1/cars/1/fuelrefills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void save_fuel_refill() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();

        given(fuelRefillService.save(1L, 1L, fuelRefillDto)).willReturn(fuelRefillDto);

        mockMvc.perform(post("/api/v1/users/1/cars/1/fuelrefills")
                .content(om.writeValueAsString(fuelRefillDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost", is(20.0)))
                .andExpect(jsonPath("$.volume", is(15.0)));
    }

    @Test
    void delete_fuel_refill() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1/cars/1/fuelrefills/1"))
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