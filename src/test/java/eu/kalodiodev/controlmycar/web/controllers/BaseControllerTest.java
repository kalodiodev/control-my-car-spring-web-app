package eu.kalodiodev.controlmycar.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kalodiodev.controlmycar.services.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
abstract class BaseControllerTest {

    protected static final String AUTHORIZATION_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiZXhwIjoxNTg5MTQzODYxLCJpYXQiOjE1ODkxMjU4NjF9.M40eLliTLQK9G4YpIPYsNNoSERobwzLGmLQiY-w9_0fD2DLd0Sm4D1wPAMuLaMRrjlsAPqZ_jwoeGBuKUIKz0g";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    AuthenticationManager authenticationManager;

}
