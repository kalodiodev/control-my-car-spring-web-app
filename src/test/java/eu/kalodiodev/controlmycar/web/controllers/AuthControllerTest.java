package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;

import eu.kalodiodev.controlmycar.services.UserService;
import eu.kalodiodev.controlmycar.web.model.authentication.AuthenticationRequest;
import eu.kalodiodev.controlmycar.web.model.authentication.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "control-my-car.kalodiodev.eu")
@WebMvcTest(value = AuthController.class)
public class AuthControllerTest extends BaseControllerTest {

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    UserService userService;

    @Test
    void register_user() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setEmail("john@example.com");
        registrationRequest.setPassword("12345678");
        registrationRequest.setPasswordConfirm("12345678");

        ConstraintDescriptions registerConstraints = new ConstraintDescriptions(RegistrationRequest.class);

        String jsonContent = om.writeValueAsString(registrationRequest);

        given(userService.register(any(RegistrationRequest.class))).willReturn(new User());
        given(jwtUtil.generateToken(any(User.class))).willReturn(AUTHORIZATION_TOKEN);

        mockMvc.perform(post("/api/v1/register")
                .content(jsonContent)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is(AUTHORIZATION_TOKEN)))
                .andExpect(status().isOk())
                .andDo(document("v1/user-register",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        attributes(key("title").value("Fields for user registration")),
                        fieldWithPath("firstName")
                            .type(JsonFieldType.STRING)
                            .description("User's first name")
                            .attributes(key("constraints").value(registerConstraints.descriptionsForProperty("firstName"))),
                        fieldWithPath("lastName")
                            .type(JsonFieldType.STRING)
                            .description("User's last name")
                            .attributes(key("constraints").value(registerConstraints.descriptionsForProperty("lastName"))),
                        fieldWithPath("email")
                                .type(JsonFieldType.STRING)
                                .description("User's email")
                                .attributes(key("constraints").value(registerConstraints.descriptionsForProperty("email"))),
                        fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .description("User's password")
                                .attributes(key("constraints").value(registerConstraints.descriptionsForProperty("password"))),
                        fieldWithPath("passwordConfirm")
                                .type(JsonFieldType.STRING)
                                .description("Password confirmation")
                                .attributes(key("constraints").value(registerConstraints.descriptionsForProperty("passwordConfirm")))
                    ),
                    responseFields(
                            fieldWithPath("token").description("Authentication token")
                    )
                ));
    }

    @Test
    void authenticate_user() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("john@example.com");
        authenticationRequest.setPassword("12345678");

        given(userDetailsService.loadUserByUsername(anyString())).willReturn(new User());
        given(jwtUtil.generateToken(any(User.class))).willReturn(AUTHORIZATION_TOKEN);

        ConstraintDescriptions authenticationConstraints = new ConstraintDescriptions(RegistrationRequest.class);

        mockMvc.perform(post("/api/v1/authenticate")
                .content(om.writeValueAsString(authenticationRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(AUTHORIZATION_TOKEN)))
                .andDo(document("v1/user-authenticate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                attributes(key("title").value("Fields for user authentication")),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("User's email")
                                        .attributes(key("constraints").value(authenticationConstraints.descriptionsForProperty("email"))),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("User's password")
                                        .attributes(key("constraints").value(authenticationConstraints.descriptionsForProperty("password")))
                        ),
                        responseFields(
                                fieldWithPath("token").description("Authentication token")
                        )
                ));
    }
}
