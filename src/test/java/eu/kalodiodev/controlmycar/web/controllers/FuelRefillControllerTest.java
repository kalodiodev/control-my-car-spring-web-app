package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.services.FuelRefillService;
import eu.kalodiodev.controlmycar.web.model.FuelRefillDto;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "control-my-car.kalodiodev.eu")
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

        refills.add(FuelRefillDto.builder()
                .id(1L)
                .cost(20d)
                .volume(15d)
                .odometer(14054d)
                .gasStation("Test Station")
                .fullRefill(true)
                .details("Nothing")
                .date(LocalDate.now())
                .carId(1L)
                .build());

        refills.add(FuelRefillDto.builder()
                .id(2L)
                .cost(25d)
                .volume(19d)
                .odometer(14479d)
                .gasStation("Test Station")
                .fullRefill(true)
                .details("Nothing")
                .date(LocalDate.now().plusWeeks(1))
                .carId(1L)
                .build());

        given(fuelRefillService.findAllByUserIdAndByCarId(anyLong(), anyLong())).willReturn(refills);

        mockMvc.perform(get("/api/v1/cars/{carId}/fuelrefills", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.fuelRefills", hasSize(2)))
                .andExpect(jsonPath("$._embedded.fuelRefills[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.fuelRefills[1].id", is(2)))
                .andDo(document("v1/fuelrefills",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        responseFields(fuelRefillsListFieldsDescriptor())
                ));
    }

    @Test
    void save_fuel_refill() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        FuelRefillDto savedFuelRefillDto = getValidFuelRefillDto();
        savedFuelRefillDto.setId(1L);
        savedFuelRefillDto.setCarId(1L);

        given(fuelRefillService.save(1L, 1L, fuelRefillDto)).willReturn(savedFuelRefillDto);

        String jsonContent = om.writeValueAsString(fuelRefillDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        mockMvc.perform(post("/api/v1/cars/{carId}/fuelrefills", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost", is(20.0)))
                .andExpect(jsonPath("$.volume", is(15.0)))
                .andDo(document("v1/fuelrefill-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car")
                        ),
                        links(
                                halLinks(),
                                linkWithRel("car-fuel-refills").description("Link to all car's fuel refills")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for fuel refill creation")),
                                fuelRefillRequestFieldsDescriptor()
                        ),
                        responseFields(
                                fuelRefillFieldsDescriptor()
                        )
                ));
    }

    @Test
    void update_fuel_refill() throws Exception {
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();

        FuelRefillDto updatedFuelRefillDto = getValidFuelRefillDto();
        updatedFuelRefillDto.setId(1L);
        updatedFuelRefillDto.setCarId(1L);

        String jsonContent = om.writeValueAsString(fuelRefillDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        given(fuelRefillService.update(1L, 1L, 1L, fuelRefillDto)).willReturn(updatedFuelRefillDto);

        mockMvc.perform(patch("/api/v1/cars/{carId}/fuelrefills/{fuelRefillId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("v1/fuelrefill-update",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car"),
                                parameterWithName("fuelRefillId").description("Id of the fuel refill")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for fuel refill update")),
                                fuelRefillRequestFieldsDescriptor()
                        )
                ));
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
        mockMvc.perform(delete("/api/v1/cars/{carId}/fuelrefills/{fuelRefillId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("v1/fuelrefill-delete",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car that the fuel refill belongs to"),
                                parameterWithName("fuelRefillId").description("Id of the fuel refill to deleted")
                        )
                ));

        verify(fuelRefillService, times(1)).delete(1L, 1L, 1L);
    }

    @Test
    void validate_date() throws Exception {
        // Date is required
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setDate(null);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());

        // Date must be in the past or present
        fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setDate(LocalDate.now().plusDays(1));
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    @Test
    void validate_details() throws Exception {
        // Details max length
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setDetails(RandomString.make(251));
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    @Test
    void validate_gas_station() throws Exception {
        // Gas station max length
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setGasStation(RandomString.make(251));
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    @Test
    void validate_odometer() throws Exception {
        // Odometer is required
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setOdometer(null);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());

        // Odometer must be positive
        fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setOdometer(-100d);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    @Test
    void validate_volume() throws Exception {
        // Volume is required
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setVolume(null);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());

        // Volume must be positive
        fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setVolume(-10d);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    @Test
    void validate_cost() throws Exception {
        // Cost is required
        FuelRefillDto fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setCost(null);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());

        // Cost must be positive
        fuelRefillDto = getValidFuelRefillDto();
        fuelRefillDto.setCost(-10d);
        post_a_new_fuel_refill(fuelRefillDto, status().is4xxClientError());
    }

    void post_a_new_fuel_refill(FuelRefillDto fuelRefillDto, ResultMatcher status) throws Exception {
        FuelRefillDto savedFuelRefillDto = getValidFuelRefillDto();
        savedFuelRefillDto.setId(1L);
        savedFuelRefillDto.setCarId(1L);

        given(fuelRefillService.save(1L, 1L, fuelRefillDto)).willReturn(savedFuelRefillDto);

        mockMvc.perform(post("/api/v1/cars/{carId}/fuelrefills", 1).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(fuelRefillDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status);
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

    private FieldDescriptor[] fuelRefillRequestFieldsDescriptor() {
        ConstraintDescriptions fuelRefillConstraints = new ConstraintDescriptions(FuelRefillDto.class);

        return new FieldDescriptor[] {
                fieldWithPath("date")
                        .type(JsonFieldType.STRING)
                        .description("Fuel refill date")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("date"))),
                fieldWithPath("odometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer reading")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("odometer"))),
                fieldWithPath("volume")
                        .type(JsonFieldType.NUMBER)
                        .description("Fuel volume")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("volume"))),
                fieldWithPath("cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Fuel refill total cost")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("cost"))),
                fieldWithPath("fullRefill")
                        .type(JsonFieldType.BOOLEAN)
                        .description("Was a full refill")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("fullRefill"))),
                fieldWithPath("details")
                        .type(JsonFieldType.STRING)
                        .description("Other details and comments")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("details"))),
                fieldWithPath("gasStation")
                        .type(JsonFieldType.STRING)
                        .description("Gas station that fuel refill took place")
                        .attributes(key("constraints")
                        .value(fuelRefillConstraints.descriptionsForProperty("gasStation")))
        };
    }

    FieldDescriptor[] fuelRefillFieldsDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Fuel refill id"),
                fieldWithPath("date").type(JsonFieldType.STRING).description("Fuel refill date"),
                fieldWithPath("odometer").type(JsonFieldType.NUMBER).description("Odometer reading"),
                fieldWithPath("volume").type(JsonFieldType.NUMBER).description("Fuel volume"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("Refill total cost"),
                fieldWithPath("fullRefill").type(JsonFieldType.BOOLEAN).description("Was a full refill"),
                fieldWithPath("details").type(JsonFieldType.STRING).description("Other details and comments"),
                fieldWithPath("gasStation").type(JsonFieldType.STRING).description("Gas station that fuel refill took place"),
                fieldWithPath("carId").type(JsonFieldType.NUMBER).description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }

    FieldDescriptor[] fuelRefillsListFieldsDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("_embedded.fuelRefills")
                        .type(JsonFieldType.ARRAY)
                        .description("Fuel refills list"),
                fieldWithPath("_embedded.fuelRefills[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("Fuel refill Id"),
                fieldWithPath("_embedded.fuelRefills[].date")
                        .type(JsonFieldType.STRING)
                        .description("Date of refill"),
                fieldWithPath("_embedded.fuelRefills[].odometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer reading"),
                fieldWithPath("_embedded.fuelRefills[].volume")
                        .type(JsonFieldType.NUMBER)
                        .description("Fuel volume"),
                fieldWithPath("_embedded.fuelRefills[].cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Refill total cost"),
                fieldWithPath("_embedded.fuelRefills[].fullRefill")
                        .type(JsonFieldType.BOOLEAN)
                        .description("Was a full refill"),
                fieldWithPath("_embedded.fuelRefills[].details")
                        .type(JsonFieldType.STRING)
                        .description("Other details and comments"),
                fieldWithPath("_embedded.fuelRefills[].gasStation")
                        .type(JsonFieldType.STRING)
                        .description("Gas station that fuel refill took place"),
                fieldWithPath("_embedded.fuelRefills[].carId")
                        .type(JsonFieldType.NUMBER)
                        .description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }
}