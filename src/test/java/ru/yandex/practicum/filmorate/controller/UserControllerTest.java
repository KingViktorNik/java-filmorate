package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final UserController controller = new UserController(new UserService(new InMemoryUserStorage()));
    private User user;

    @BeforeEach
    void addUser() {
        user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));

        user = controller.create(user);
    }

    @Test
    void create() {
        user = controller.create(user);

        assertFalse(controller.userAll().isEmpty());
        assertEquals("Jimi",controller.user(user.getId()).getName());
    }

    @Test
    void createNullName() {
        user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990,5,5));

        user = controller.create(user);

        assertFalse(controller.userAll().isEmpty());
        assertEquals("Jim878",controller.user(user.getId()).getName());
    }

    @Test
    void createBlankName() {
        User user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("    ");
        user.setBirthday(LocalDate.of(1990,5,5));

        user = controller.create(user);

        assertFalse(controller.userAll().isEmpty());
        assertEquals("Jim878",controller.user(user.getId()).getName());
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

        assertNotEquals(user, controller.userAll().get(0));
        assertEquals("JimTheWorm", controller.userAll().get(0).getName());
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
        user = controller.create(user);
        User user2 = controller.user(user.getId());
        assertEquals(user, user2);
    }

    @Test
    void nullUserId() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.user(555L)
        );
        assertEquals("Ползователя с id: '555' не существует!",exception.getMessage());

    }

    @Test
    void userAll() {
        List<User> userAll = controller.userAll();
        assertNotEquals(0, userAll.size());
    }

    @Test
    void addHappyFriend() {
        User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        controller.create(friend);

        assertEquals(2, controller.userAll().size());

        controller.addFriend(user.getId(), friend.getId());

        List<Long> userList = new ArrayList<>(controller.user(user.getId()).getFriends());
        List<Long> friendList = new ArrayList<>(controller.user(friend.getId()).getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.get(0), user.getId());
    }

    @Test
    void addFriendNullUser() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.addFriend(555L,user.getId())
        );
        assertEquals("Пользователь с id:555 несуществует.",exception.getMessage());
    }

    @Test
    void addFriendNullFriend() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.addFriend(user.getId(), 555L)
        );
        assertEquals("Пользователь с id:555 несуществует.",exception.getMessage());
    }

    @Test
    void addYourselfAsFriends() {
        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.addFriend(user.getId(), user.getId())
        );
        assertEquals("Пользователь не может добвасить сам себя в друзья!",exception.getMessage());
    }

    @Test
    void deleteHappyFriend() {
        User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        friend = controller.create(friend);

        user = controller.addFriend(user.getId(), friend.getId());

        List<Long> userList = new ArrayList<>(user.getFriends());
        List<Long> friendList = new ArrayList<>(friend.getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.get(0), user.getId());

        user = controller.deleteFriend(user.getId(), friend.getId());

        assertTrue(user.getFriends().isEmpty());
        assertTrue(controller.user(friend.getId()).getFriends().isEmpty());
    }

    @Test
    void deleteFriendNullUser() {
        User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        long friendId = controller.create(friend).getId();

        controller.addFriend(user.getId(), friend.getId());

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.deleteFriend(555L, friendId)
        );
        assertEquals("Пользователь с id:555 несуществует.",exception.getMessage());
    }

    @Test
    void deleteFriendNullFriend() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.deleteFriend(user.getId(), 555L)
        );
        assertEquals("Пользователь с id:555 несуществует.",exception.getMessage());
    }

    @Test
    void listFriends() {
        User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        friend = controller.create(friend);

        user = controller.addFriend(user.getId(), friend.getId());
        List<User> users = controller.listFriends(user.getId());
        assertEquals("bobic@mail.com", users.get(0).getEmail());
    }

    @Test
    void commonFriends() {
        User friend1 = new User();
        friend1.setEmail("friend1@mail.com");
        friend1.setLogin("friend1");
        friend1.setName("friend1");
        friend1.setBirthday(LocalDate.of(1991,6,15));

        friend1 = controller.create(friend1);

        User friend2 = new User();
        friend2.setEmail("friend2@mail.com");
        friend2.setLogin("friend2");
        friend2.setName("friend2");
        friend2.setBirthday(LocalDate.of(1991,6,15));

        friend2 = controller.create(friend2);

        User friend3 = new User();
        friend3.setEmail("friend3@mail.com");
        friend3.setLogin("friend3");
        friend3.setName("friend3");
        friend3.setBirthday(LocalDate.of(1991,6,15));

        friend3 = controller.create(friend3);

        controller.addFriend(user.getId(), friend1.getId());
        user = controller.addFriend(user.getId(), friend2.getId());
        friend3 = controller.addFriend(friend3.getId(), friend2.getId());

        List<User> commonFriends = controller.commonFriends(user.getId(), friend3.getId());

        assertFalse(commonFriends.isEmpty());
        assertEquals(1, commonFriends.size());
        assertEquals(friend2.getEmail(), commonFriends.get(0).getEmail());
    }
}