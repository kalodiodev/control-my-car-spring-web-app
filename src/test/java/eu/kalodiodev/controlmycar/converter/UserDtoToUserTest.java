package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.web.model.CarDto;
import eu.kalodiodev.controlmycar.web.model.UserDto;
import eu.kalodiodev.controlmycar.domains.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserDtoToUserTest {

    private static final Long ID_VALUE = 1L;
    private static final String FIRST_NAME_VALUE = "John";
    private static final String LAST_NAME_VALUE = "Doe";
    private static final String EMAIL_VALUE = "john@example.com";
    private static final String PASSWORD_VALUE = "password";
    private static final Long CAR_ID_VALUE = 1L;


    private UserDtoToUser converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new UserDtoToUser(new CarDtoToCar());
    }

    @Test
    void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new UserDto()));
    }

    @Test
    public void convert() throws Exception {
        // given
        UserDto userDto = new UserDto();
        userDto.setId(ID_VALUE);
        userDto.setFirstName(FIRST_NAME_VALUE);
        userDto.setLastName(LAST_NAME_VALUE);
        userDto.setEmail(EMAIL_VALUE);
        userDto.setPassword(PASSWORD_VALUE);
        userDto.setPasswordConfirm(PASSWORD_VALUE);

        CarDto carDto = new CarDto();
        carDto.setId(CAR_ID_VALUE);

        userDto.getCars().add(carDto);

        // when
        User user = converter.convert(userDto);

        // then
        assertNotNull(user);
        assertEquals(ID_VALUE, user.getId());
        assertEquals(FIRST_NAME_VALUE, user.getFirstName());
        assertEquals(LAST_NAME_VALUE, user.getLastName());
        assertEquals(EMAIL_VALUE, user.getEmail());
        assertEquals(PASSWORD_VALUE, user.getPassword());
        assertEquals(1, user.getCars().size());
    }
}
