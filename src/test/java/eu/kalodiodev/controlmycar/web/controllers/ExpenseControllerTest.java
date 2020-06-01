package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "control-my-car.kalodiodev.eu")
@WebMvcTest(ExpenseController.class)
@Import(SecurityTestConfig.class)
class ExpenseControllerTest extends BaseControllerTest {

    @MockBean
    ExpenseService expenseService;

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
        List<ExpenseDto> expenses = new ArrayList<>();

        expenses.add(ExpenseDto.builder()
                .id(1L)
                .date(LocalDate.now())
                .title("Vehicle Tax")
                .description("Vehicle tax payment for year 2020")
                .cost(230d)
                .carId(1L)
                .build());

        expenses.add(ExpenseDto.builder()
                .id(2L)
                .date(LocalDate.now())
                .title("Insurance")
                .description("Car insurance")
                .cost(680d)
                .carId(1L)
                .build());

        given(expenseService.findAllByUserIdAndByCarId(anyLong(), anyLong())).willReturn(expenses);

        mockMvc.perform(get("/api/v1/cars/{carId}/expenses", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.expenses", hasSize(2)))
                .andExpect(jsonPath("$._embedded.expenses[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.expenses[1].id", is(2)))
                .andDo(document("v1/expenses",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        responseFields(
                                servicesListFieldsDescriptor()
                        )
                ));
    }

    private FieldDescriptor[] servicesListFieldsDescriptor() {
        return new FieldDescriptor[]{
                fieldWithPath("_embedded.expenses")
                        .type(JsonFieldType.ARRAY)
                        .description("Expenses list"),
                fieldWithPath("_embedded.expenses[].id")
                        .type(JsonFieldType.NUMBER)
                        .description("Expense Id"),
                fieldWithPath("_embedded.expenses[].date")
                        .type(JsonFieldType.STRING)
                        .description("Date of expense"),
                fieldWithPath("_embedded.expenses[].title")
                        .type(JsonFieldType.STRING)
                        .description("Title of expense"),
                fieldWithPath("_embedded.expenses[].description")
                        .type(JsonFieldType.STRING)
                        .description("Description of expense"),
                fieldWithPath("_embedded.expenses[].cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Expense total cost"),
                fieldWithPath("_embedded.expenses[].carId")
                        .type(JsonFieldType.NUMBER)
                        .description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }
}