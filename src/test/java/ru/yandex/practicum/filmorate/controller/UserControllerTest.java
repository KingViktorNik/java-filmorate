package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    private final UserController controller = new UserController();

    @Test
    void create() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        controller.create(user);

        assertFalse(controller.users.isEmpty());
        assertEquals("Jimi",controller.users.get(1).getName());
    }

    @Test
    void createNullName() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990,05,05));

        controller.create(user);

        assertFalse(controller.users.isEmpty());
        assertEquals("Jim878",controller.users.get(1).getName());
    }

    @Test
    void createBlankName() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("    ");
        user.setBirthday(LocalDate.of(1990,05,05));

        controller.create(user);

        assertFalse(controller.users.isEmpty());
        assertEquals("Jim878",controller.users.get(1).getName());
    }

    @Test
    void createNullLogin() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin(null);
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("login не коректный.",exception.getMessage());
    }

    @Test
    void createBlankLogin() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("      ");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("login не коректный.",exception.getMessage());
    }

    @Test
    void createNullEmail() {
        final User user = new User();
        user.setEmail(null);
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("email не коректный.",exception.getMessage());
    }

    @Test
    void createBlankEmail() {
        final User user = new User();
        user.setEmail("             ");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("email не коректный.",exception.getMessage());
    }

    @Test
    void createIncorrectEmail() {
        final User user = new User();
        user.setEmail("jimimailcom");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("email не коректный.",exception.getMessage());
    }

    @Test
    void createIncorrectBirthday() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(2023,05,05));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(user)
        );
        assertEquals("Дата не может быть в будущем.",exception.getMessage());
    }

    @Test
    void update() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        controller.create(user);

        final User userNew = new User();
        userNew.setId(1);
        userNew.setEmail("jimi@mail.com");
        userNew.setLogin("JimTheWorm");
        userNew.setName(null);
        userNew.setBirthday(LocalDate.of(1990,05,05));

        controller.update(userNew);

        assertNotEquals(user, controller.users.get(1));
        assertEquals("JimTheWorm", controller.users.get(1).getName());
    }

    @Test
    void updateException() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));

        controller.create(user);
        user.setId(555);

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.update(user)
        );
        assertEquals("Ползователя с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userAll() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,05,05));
        controller.create(user);
        List<User> userAll = controller.userAll();
        assertEquals(user, userAll.get(0));
    }
}