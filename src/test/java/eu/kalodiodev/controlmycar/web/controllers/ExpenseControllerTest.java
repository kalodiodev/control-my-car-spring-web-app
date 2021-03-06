package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.SecurityTestConfig;
import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.ExpenseService;
import eu.kalodiodev.controlmycar.web.model.ExpenseDto;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
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
                                expensesListFieldsDescriptor()
                        )
                ));
    }

    @Test
    void save_service() throws Exception {
        ExpenseDto expenseDto = getValidExpenseDto();
        ExpenseDto savedExpenseDto = getValidExpenseDto();
        savedExpenseDto.setId(1L);
        savedExpenseDto.setCarId(1L);

        given(expenseService.save(1L, 1L, expenseDto)).willReturn(savedExpenseDto);

        String jsonContent = om.writeValueAsString(expenseDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        mockMvc.perform(post("/api/v1/cars/{carId}/expenses", 1L).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost", is(20.0)))
                .andExpect(jsonPath("$.title", is("Expense title")))
                .andDo(document("v1/expense-create",
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
                                linkWithRel("car-expenses").description("Link to all car's expenses")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for expense creation")),
                                expenseRequestFieldsDescriptor()
                        ),
                        responseFields(
                                expenseFieldsDescriptor()
                        )
                ));
    }

    @Test
    void update_expense() throws Exception {
        ExpenseDto expenseDto = getValidExpenseDto();

        ExpenseDto updatedExpenseDto = getValidExpenseDto();
        updatedExpenseDto.setId(1L);
        updatedExpenseDto.setCarId(1L);

        String jsonContent = om.writeValueAsString(expenseDto)
                .replace("\"id\":null,", "")
                .replace("\"carId\":null,", "")
                .replace(",\"links\":[]", "");

        given(expenseService.update(1L, 1L, 1L, expenseDto)).willReturn(updatedExpenseDto);

        mockMvc.perform(patch("/api/v1/cars/{carId}/expenses/{expenseId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("v1/expense-update",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car"),
                                parameterWithName("expenseId").description("Id of the expense")
                        ),
                        requestFields(
                                attributes(key("title").value("Fields for service update")),
                                expenseRequestFieldsDescriptor()
                        )
                ));
    }

    @Test
    void delete_expense() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/{carId}/expenses/{expenseId}", 1L, 1L)
                .with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("v1/expense-delete",
                        requestHeaders(
                                headerWithName("Authorization").description("JWT authentication")
                        ),
                        pathParameters(
                                parameterWithName("carId").description("Id of the car that expense belongs to"),
                                parameterWithName("expenseId").description("Id of the expense to delete")
                        )
                ));

        verify(expenseService, times(1)).delete(1L, 1L, 1L);
    }

    @Test
    void validate_date() throws Exception {
        // Date is required
        ExpenseDto expenseDto = getValidExpenseDto();
        expenseDto.setDate(null);
        post_a_new_expense(expenseDto, status().is4xxClientError());

        // Date must be in the past or present
        expenseDto = getValidExpenseDto();
        expenseDto.setDate(LocalDate.now().plusDays(1));
        post_a_new_expense(expenseDto, status().is4xxClientError());
    }

    @Test
    void validate_title() throws Exception {
        // Title required
        ExpenseDto expenseDto = getValidExpenseDto();
        expenseDto.setTitle("");
        post_a_new_expense(expenseDto, status().is4xxClientError());

        // Title min length
        expenseDto = getValidExpenseDto();
        expenseDto.setTitle(RandomString.make(2));
        post_a_new_expense(expenseDto, status().is4xxClientError());

        // Title max length
        expenseDto = getValidExpenseDto();
        expenseDto.setTitle(RandomString.make(191));
        post_a_new_expense(expenseDto, status().is4xxClientError());
    }

    @Test
    void validate_description() throws Exception {
        // Description max length
        ExpenseDto expenseDto = getValidExpenseDto();
        expenseDto.setTitle(RandomString.make(256));
        post_a_new_expense(expenseDto, status().is4xxClientError());
    }

    @Test
    void validate_cost() throws Exception {
        // Cost is required
        ExpenseDto expenseDto = getValidExpenseDto();
        expenseDto.setCost(null);
        post_a_new_expense(expenseDto, status().is4xxClientError());
    }

    void post_a_new_expense(ExpenseDto expenseDto, ResultMatcher status) throws Exception {
        ExpenseDto savedExpenseDto = getValidExpenseDto();
        savedExpenseDto.setId(1L);
        savedExpenseDto.setCarId(1L);

        given(expenseService.save(1L, 1L, expenseDto)).willReturn(savedExpenseDto);

        mockMvc.perform(post("/api/v1/cars/{carId}/expenses", 1).with(user(authenticatedUser))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTHORIZATION_TOKEN)
                .content(om.writeValueAsString(expenseDto))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status);
    }

    private ExpenseDto getValidExpenseDto() {
        return ExpenseDto.builder()
                .date(LocalDate.now())
                .title("Expense title")
                .description("Expense description")
                .cost(20d)
                .build();
    }

    private FieldDescriptor[] expenseRequestFieldsDescriptor() {
        ConstraintDescriptions expenseConstraints = new ConstraintDescriptions(ExpenseDto.class);

        return new FieldDescriptor[] {
                fieldWithPath("date")
                        .type(JsonFieldType.STRING)
                        .description("Expense date")
                        .attributes(key("constraints")
                        .value(expenseConstraints.descriptionsForProperty("date"))),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description("Expense title")
                        .attributes(key("constraints")
                        .value(expenseConstraints.descriptionsForProperty("title"))),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description("Expense description")
                        .attributes(key("constraints")
                        .value(expenseConstraints.descriptionsForProperty("description"))),
                fieldWithPath("cost")
                        .type(JsonFieldType.NUMBER)
                        .description("Expense total cost")
                        .attributes(key("constraints")
                        .value(expenseConstraints.descriptionsForProperty("cost")))
        };
    }

    private FieldDescriptor[] expenseFieldsDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Expense id"),
                fieldWithPath("date").type(JsonFieldType.STRING).description("Expense date"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("Expense title"),
                fieldWithPath("description").type(JsonFieldType.STRING).description("Expense description"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("Expense total cost"),
                fieldWithPath("carId").type(JsonFieldType.NUMBER).description("The id of the car"),
                subsectionWithPath("_links").ignored()
        };
    }

    private FieldDescriptor[] expensesListFieldsDescriptor() {
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