package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserStorageTest {


    private final UserStorage controller  = new InMemoryUserStorage();


    @Test
    void create() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));

        controller.create(user);

        assertFalse(controller.getUsers().isEmpty());
        assertEquals("Jimi",controller.getUsers().get(1L).getName());
    }

    @Test
    void createNullName() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990,5,5));

        controller.create(user);

        assertFalse(controller.getUsers().isEmpty());
        assertEquals("Jim878",controller.getUsers().get(1L).getName());
    }

    @Test
    void createBlankName() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("    ");
        user.setBirthday(LocalDate.of(1990,5,5));

        controller.create(user);

        assertFalse(controller.getUsers().isEmpty());
        assertEquals("Jim878",controller.getUsers().get(1L).getName());
    }

    @Test
    void createNullLogin() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin(null);
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));

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
        user.setBirthday(LocalDate.of(1990,5,5));

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
        user.setBirthday(LocalDate.of(1990,5,5));

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
        user.setBirthday(LocalDate.of(1990,5,5));

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
        user.setBirthday(LocalDate.of(1990,5,5));

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
        user.setBirthday(LocalDate.of(2023,5,5));

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
        user.setBirthday(LocalDate.of(1990,5,5));

        controller.create(user);

        final User userNew = new User();
        userNew.setId(1);
        userNew.setEmail("jimi@mail.com");
        userNew.setLogin("JimTheWorm");
        userNew.setName(null);
        userNew.setBirthday(LocalDate.of(1990,5,5));

        controller.update(userNew);

        assertNotEquals(user, controller.getUsers().get(1L));
        assertEquals("JimTheWorm", controller.getUsers().get(1L).getName());
    }

    @Test
    void updateException() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));

        controller.create(user);
        user.setId(555);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.update(user)
        );
        assertEquals("Ползователя с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userIncorrectId() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));
        controller.create(user);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.user(555)
        );
        assertEquals("Ползователя с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userId() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));
        controller.create(user);
        User user2 = controller.user(1L);
        assertEquals(user, user2);
    }

    @Test
    void userAll() {
        final User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));
        controller.create(user);
        List<User> userAll = controller.userAll();
        assertEquals(user, userAll.get(0));
    }
}