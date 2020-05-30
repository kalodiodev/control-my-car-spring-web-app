package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.exceptions.NotFoundException;
import eu.kalodiodev.controlmycar.services.ServiceService;
import eu.kalodiodev.controlmycar.web.model.ServiceDto;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "control-my-car.kalodiodev.eu")
@WebMvcTest(ServiceController.class)
@Import(SecurityTestConfig.class)
class ServiceControllerTest extends BaseControllerTest {

    @MockBean
    ServiceService serviceService;

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
    void all_car_services() throws Exception {
        List<ServiceDto> services = new ArrayList<>();

        services.add(ServiceDto.builder()
                .id(1L)
                .date(LocalDate.now())
                .title("Oil change")
                .description("Oil and oil filter change")
                .odometer(15334d)
                .cost(230d)
                .carId(1L)
                .build());

        services.add(ServiceDto.builder()
                .id(2L)
                .date(LocalDate.now())
                .title("Tyre change")
                .description("Replace tyres")
                .odometer(18525d)
                .cost(680d)
                .carId(1L)
                .build());

        given(serviceService.findAllByUserIdAndByCarId(anyLong(), anyLong())).willReturn(services);

        mockMvc.perform(get("/api/v1/cars/{carId}/services", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.services", hasSize(2)))
                .andExpect(jsonPath("$._embedded.services[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.services[1].id", is(2)))
                .andDo(document("v1/services",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        responseFields(
                                servicesListFieldsDescriptor()
                        )
                ));
    }

    @Test
    void save_service() throws Exception {
        ServiceDto serviceDto = getValidServiceDto();
        ServiceDto savedServiceDto = getValidServiceDto();
        savedServiceDto.setId(1L);
        savedServiceDto.setCarId(1L);

        given(serviceService.save(1L, 1L, serviceDto)).willReturn(savedServiceDto);

        String jsonContent = om.writeValueAsString(serviceDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        mockMvc.perform(post("/api/v1/cars/{carId}/services", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost", is(20.0)))
                .andExpect(jsonPath("$.title", is("Service title")))
                .andDo(document("v1/service-create",
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
                                linkWithRel("car-services").description("Link to all car's services")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for service creation")),
                                serviceRequestFieldsDescriptor()
                        ),
                        responseFields(
                                serviceFieldsDescriptor()
                        )
                ));
    }

    @Test
    void update_service_not_found() throws Exception {
        ServiceDto serviceDto = getValidServiceDto();

        given(serviceService.update(1L, 1L, 1L, serviceDto)).willThrow(NotFoundException.class);

        mockMvc.perform(patch("/api/v1/cars/1/services/1")
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(serviceDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_service() throws Exception {
        ServiceDto serviceDto = getValidServiceDto();

        ServiceDto updatedServiceDto = getValidServiceDto();
        updatedServiceDto.setId(1L);
        updatedServiceDto.setCarId(1L);

        String jsonContent = om.writeValueAsString(serviceDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        given(serviceService.update(1L, 1L, 1L, serviceDto)).willReturn(updatedServiceDto);

        mockMvc.perform(patch("/api/v1/cars/{carId}/services/{serviceId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("v1/service-update",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car"),
                                parameterWithName("serviceId").description("Id of the service")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for service update")),
                                serviceRequestFieldsDescriptor()
                        )
                ));
    }

    @Test
    void delete_service() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/{carId}/services/{serviceId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("v1/service-delete",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car that service belongs to"),
                                parameterWithName("serviceId").description("Id of the service to delete")
                        )
                ));

        verify(serviceService, times(1)).delete(1L, 1L, 1L);
    }

    private ServiceDto getValidServiceDto() {
        return ServiceDto.builder()
                .date(LocalDate.now())
                .title("Service title")
                .description("Service description")
                .odometer(132434d)
                .cost(20d)
                .build();
    }

    private FieldDescriptor[] serviceRequestFieldsDescriptor() {
        ConstraintDescriptions serviceConstraints = new ConstraintDescriptions(ServiceDto.class);

        return new FieldDescriptor[] {
                fieldWithPath("date")
                        .type(JsonFieldType.STRING)
                        .description("Service date")
                        .attributes(key("constraints")
                        .value(serviceConstraints.descriptionsForProperty("date"))),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description("Service title")
                        .attributes(key("constraints")
                        .value(serviceConstraints.descriptionsForProperty("title"))),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description("Service description")
                        .attributes(key("constraints")
                        .value(serviceConstraints.descriptionsForProperty("description"))),
                fieldWithPath("odometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer reading")
                        .attributes(key("constraints")
                        .value(serviceConstraints.descriptionsForProperty("odometer"))),
                fieldWithPath("cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Service total cost")
                        .attributes(key("constraints")
                        .value(serviceConstraints.descriptionsForProperty("cost")))
        };
    }

    private FieldDescriptor[] serviceFieldsDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Service id"),
                fieldWithPath("date").type(JsonFieldType.STRING).description("Service date"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("Service title"),
                fieldWithPath("description").type(JsonFieldType.STRING).description("Service description"),
                fieldWithPath("odometer").type(JsonFieldType.NUMBER).description("Odometer reading"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("Service total cost"),
                fieldWithPath("carId").type(JsonFieldType.NUMBER).description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }

    private FieldDescriptor[] servicesListFieldsDescriptor() {
        return new FieldDescriptor[]{
                fieldWithPath("_embedded.services")
                        .type(JsonFieldType.ARRAY)
                        .description("Services list"),
                fieldWithPath("_embedded.services[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("Service Id"),
                fieldWithPath("_embedded.services[].date")
                        .type(JsonFieldType.STRING)
                        .description("Date of service"),
                fieldWithPath("_embedded.services[].title")
                        .type(JsonFieldType.STRING)
                        .description("Title of service"),
                fieldWithPath("_embedded.services[].description")
                        .type(JsonFieldType.STRING)
                        .description("Description of service"),
                fieldWithPath("_embedded.services[].odometer")
                        .type(JsonFieldType.NUMBER)
                        .description("Odometer reading"),
                fieldWithPath("_embedded.services[].cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Service total cost"),
                fieldWithPath("_embedded.services[].carId")
                        .type(JsonFieldType.NUMBER)
                        .description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }
}