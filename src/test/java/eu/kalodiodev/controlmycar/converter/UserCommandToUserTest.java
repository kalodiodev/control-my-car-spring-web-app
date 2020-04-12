package eu.kalodiodev.controlmycar.converter;

import eu.kalodiodev.controlmycar.command.CarCommand;
import eu.kalodiodev.controlmycar.command.UserCommand;
import eu.kalodiodev.controlmycar.domains.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserCommandToUserTest {

    private static final Long ID_VALUE = 1L;
    private static final String FIRST_NAME_VALUE = "John";
    private static final String LAST_NAME_VALUE = "Doe";
    private static final String EMAIL_VALUE = "john@example.com";
    private static final String PASSWORD_VALUE = "password";
    private static final Long CAR_ID_VALUE = 1L;


    private UserCommandToUser converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new UserCommandToUser(new CarCommandToCar());
    }

    @Test
    void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new UserCommand()));
    }

    @Test
    public void convert() throws Exception {
        // given
        UserCommand command = new UserCommand();
        command.setId(ID_VALUE);
        command.setFirstName(FIRST_NAME_VALUE);
        command.setLastName(LAST_NAME_VALUE);
        command.setEmail(EMAIL_VALUE);
        command.setPassword(PASSWORD_VALUE);
        command.setPasswordConfirm(PASSWORD_VALUE);

        CarCommand carCommand = new CarCommand();
        carCommand.setId(CAR_ID_VALUE);

        command.getCars().add(carCommand);

        // when
        User user = converter.convert(command);

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
