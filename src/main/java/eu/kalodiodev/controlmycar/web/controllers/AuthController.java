package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.domains.User;
import eu.kalodiodev.controlmycar.services.UserService;
import eu.kalodiodev.controlmycar.services.security.JwtUtil;
import eu.kalodiodev.controlmycar.web.model.authentication.AuthenticationRequest;
import eu.kalodiodev.controlmycar.web.model.authentication.AuthenticationResponse;
import eu.kalodiodev.controlmycar.web.model.authentication.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping(value = "authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) throws Exception {

        User user = userService.register(request);

        final String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
