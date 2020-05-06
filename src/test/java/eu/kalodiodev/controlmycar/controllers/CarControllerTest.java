package eu.kalodiodev.controlmycar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.web.controllers.CarController;
import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.domains.Car;
import eu.kalodiodev.controlmycar.services.CarService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "control-my-car.kalodiodev.eu")
@WebMvcTest(CarController.class)
public class CarControllerTest {

    @MockBean
    CarService carService;

    @Autowired
    ObjectMapper om;

    @Autowired
    MockMvc mockMvc;

    CarDto carDto1;
    CarDto carDto2;

    @BeforeEach
    void setUp() {
        carDto1 = CarDto.builder()
                .id(1L)
                .userId(1L)
                .manufacturer("Nissan")
                .model("Micra")
                .boughtPrice(10000d)
                .initialOdometer(1000d)
                .manufacturedYear(2009)
                .ownedYear(2010)
                .numberPlate("AAA-1234")
                .build();

        carDto2 = CarDto.builder()
                .id(2L)
                .userId(1L)
                .manufacturer("Toyota")
                .model("Yaris")
                .boughtPrice(5230d)
                .initialOdometer(120500d)
                .manufacturedYear(2012)
                .ownedYear(2012)
                .numberPlate("BBB-1234")
                .build();
    }


    @Test
    void all_user_cars() throws Exception {
        Set<CarDto> cars = new HashSet<>();
        cars.add(carDto1);
        cars.add(carDto2);

        given(carService.allOfUser(anyLong())).willReturn(cars);

        mockMvc.perform(get("/api/v1/users/1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cars", hasSize(2)))
                .andExpect(jsonPath("$._embedded.cars[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.cars[1].id", is(2)))
                .andDo(document("v1/cars",
                        preprocessResponse(prettyPrint()),
                        responseFields(carsListFieldsDescriptor())
                ));
    }

    @Test
    void find_car() throws Exception {
        Car car = new Car();
        car.setId(1L);

        CarDto carDto = getValidCarDto();
        carDto.setId(1L);
        carDto.setUserId(1L);

        given(carService.findByUserIdAndCarId(1L, 1L)).willReturn(carDto);

        mockMvc.perform(get("/api/v1/users/{userId}/cars/{carId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andDo(document("v1/car",
                        preprocessResponse(prettyPrint()),
                        links(
                            halLinks(),
                            linkWithRel("self").ignored()
                        ),
                        pathParameters(
                            parameterWithName("userId").description("User id that owns the car"),
                            parameterWithName("carId").description("Car id of the desired car to get.")
                        ),
                        responseFields(carFieldsDescriptor())
                ));
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

        given(carService.save(1L, carDto)).willReturn(carDto1);

        mockMvc.perform(post("/api/v1/users/{userId}/cars", 1L)
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is("Micra")))
                .andDo(document("v1/car-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        links(
                                halLinks(),
                                linkWithRel("self").ignored(),
                                linkWithRel("all-cars").description("Link to all user cars")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("User id that owns the car")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for car creation")),
                                carRequestFieldsDescriptor()
                        ),
                        responseFields(
                                carFieldsDescriptor()
                        )
                ));
    }

    @Test
    void update_car() throws Exception {
        CarDto carDto = getValidCarDto();

        mockMvc.perform(patch("/api/v1/users/{userId}/cars/{carId}", 1L, 3L)
                .content(om.writeValueAsString(carDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("v1/car-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("User id that owns the car"),
                                parameterWithName("carId").description("Id of the car to update")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for car creation")),
                                carRequestFieldsDescriptor()
                        )
                ));
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
        mockMvc.perform(delete("/api/v1/users/{userId}/cars/{carId}", 1L, 3L))
                .andExpect(status().isNoContent())
                .andDo(document("v1/car-delete",
                        pathParameters(
                                parameterWithName("userId").description("User id that owns the car"),
                                parameterWithName("carId").description("Id of the car to be deleted")
                        )
                ));

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

    FieldDescriptor[] carsListFieldsDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("_embedded.cars")
                        .type(JsonFieldType.ARRAY)
                        .description("Cars list"),
                fieldWithPath("_embedded.cars[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("Car Id"),
                fieldWithPath("_embedded.cars[].numberPlate")
                        .type(JsonFieldType.STRING)
                        .description("Car license number plate"),
                fieldWithPath("_embedded.cars[].manufacturer")
                        .type(JsonFieldType.STRING)
                        .description("Car manufacturer"),
                fieldWithPath("_embedded.cars[].model")
                        .type(JsonFieldType.STRING)
                        .description("Car model"),
                fieldWithPath("_embedded.cars[].manufacturedYear")
                        .type(JsonFieldType.STRING)
                        .description("Car's year of manufacture"),
                fieldWithPath("_embedded.cars[].ownedYear")
                        .type(JsonFieldType.STRING)
                        .description("Year when user bought the car"),
                fieldWithPath("_embedded.cars[].boughtPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("The price the user bought the car"),
                fieldWithPath("_embedded.cars[].initialOdometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer value when user bought the car"),
                fieldWithPath("_embedded.cars[].userId")
                        .type(JsonFieldType.NUMBER)
                        .description("User id that owns the car"),
                subsectionWithPath("_embedded.cars[]._links")
                        .description("Links related to this car"),
                subsectionWithPath("_links")
                        .ignored()
        };
    }

    FieldDescriptor[] carFieldsDescriptor () {
        return new FieldDescriptor[] {
                fieldWithPath("id")
                        .type(JsonFieldType.NUMBER)
                        .description("Car Id"),
                fieldWithPath("numberPlate")
                        .type(JsonFieldType.STRING)
                        .description("Car license number plate"),
                fieldWithPath("manufacturer")
                        .type(JsonFieldType.STRING)
                        .description("Car manufacturer"),
                fieldWithPath("model")
                        .type(JsonFieldType.STRING)
                        .description("Car model"),
                fieldWithPath("manufacturedYear")
                        .type(JsonFieldType.STRING)
                        .description("Car's year of manufacture"),
                fieldWithPath("ownedYear")
                        .type(JsonFieldType.STRING)
                        .description("Year when user bought the car"),
                fieldWithPath("boughtPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("The price the user bought the car"),
                fieldWithPath("initialOdometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer value when user bought the car"),
                fieldWithPath("userId")
                        .type(JsonFieldType.NUMBER)
                        .description("User id that owns the car"),
                subsectionWithPath("_links")
                        .ignored()
        };
    }

    FieldDescriptor[] carRequestFieldsDescriptor() {
        ConstraintDescriptions carConstraints = new ConstraintDescriptions(CarDto.class);

        return new FieldDescriptor[] {
                fieldWithPath("id").ignored(),
                fieldWithPath("userId").ignored(),
                fieldWithPath("links").ignored(),
                fieldWithPath("numberPlate")
                        .type(JsonFieldType.STRING)
                        .description("Car's license number plate")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("numberPlate"))),
                fieldWithPath("manufacturer")
                        .type(JsonFieldType.STRING)
                        .description("Car manufacturer")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("manufacturer"))),
                fieldWithPath("model")
                        .type(JsonFieldType.STRING)
                        .description("Car model")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("model"))),
                fieldWithPath("manufacturedYear")
                        .type(JsonFieldType.STRING)
                        .description("The year the car was manufactured")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("manufacturedYear"))),
                fieldWithPath("boughtPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("The price the user bought the car")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("boughtPrice"))),
                fieldWithPath("initialOdometer")
                        .type(JsonFieldType.NUMBER)
                        .description("The odometer reading when the user bought the car")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("initialOdometer"))),
                fieldWithPath("ownedYear")
                        .type(JsonFieldType.STRING)
                        .description("The year the user bought the car")
                        .attributes(key("constraints").value(carConstraints.descriptionsForProperty("ownedYear")))
        };
    }
}
