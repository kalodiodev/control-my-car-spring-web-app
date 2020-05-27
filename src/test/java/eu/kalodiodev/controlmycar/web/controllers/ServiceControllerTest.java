package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    FieldDescriptor[] serviceFieldsDescriptor() {
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

    FieldDescriptor[] servicesListFieldsDescriptor() {
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