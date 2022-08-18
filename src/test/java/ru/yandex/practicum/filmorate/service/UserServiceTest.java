package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());
    private User user;

    @BeforeEach
    void addUser() {
        user = new User();
        user.setEmail("jimi@mail.com");
        user.setLogin("Jim878");
        user.setName("Jimi");
        user.setBirthday(LocalDate.of(1990,5,5));

        user = userService.getUserStorage().create(user);
    }

    @AfterEach
    void clearUsers() {
        userService.getUserStorage().getUsers().clear();
    }

    @Test
    void addHappyFriend() {
        final User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend);

        assertEquals(2, userService.getUserStorage().getUsers().size());

        userService.addFriend(user.getId(), friend.getId());

        List<Long> userList = new ArrayList<>(userService.getUserStorage().getUsers().get(user.getId()).getFriends());
        List<Long> friendList = new ArrayList<>(userService.getUserStorage().getUsers().get(friend.getId()).getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.get(0), user.getId());
    }

    @Test
    void addNullFriend() {
        final User friend = new User();
        friend.setId(555);

        assertEquals(1, userService.getUserStorage().getUsers().size());

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> userService.addFriend(user.getId(), friend.getId())
        );
        assertEquals("Пользователь с id:555 несуществует.",exception.getMessage());
    }

    @Test
    void addYourselfAsFriends() {
        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.addFriend(user.getId(), user.getId())
        );
        assertEquals("Пользователь не может добвасить сам себя в друзья!",exception.getMessage());
    }

    @Test
    void deleteHappyFriend() {
        final User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend);

        assertEquals(2, userService.getUserStorage().getUsers().size());

        userService.addFriend(user.getId(), friend.getId());

        List<Long> userList = new ArrayList<>(userService.getUserStorage().getUsers().get(user.getId()).getFriends());
        List<Long> friendList = new ArrayList<>(userService.getUserStorage().getUsers().get(friend.getId()).getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.get(0), user.getId());

        userService.deleteFriend(user.getId(), friend.getId());

        assertTrue(userService.getUserStorage().getUsers().get(user.getId()).getFriends().isEmpty());
        assertTrue(userService.getUserStorage().getUsers().get(friend.getId()).getFriends().isEmpty());
    }

    @Test
    void listFriends() {
        final User friend = new User();
        friend.setEmail("bobic@mail.com");
        friend.setLogin("Bond");
        friend.setName("Bob");
        friend.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend);

        assertEquals(2, userService.getUserStorage().getUsers().size());

        userService.addFriend(user.getId(), friend.getId());
        List<User> users = userService.listFriends(user.getId());
        assertEquals("bobic@mail.com", users.get(0).getEmail());
    }

    @Test
    void commonFriends() {
        final User friend1 = new User();
        friend1.setEmail("friend1@mail.com");
        friend1.setLogin("friend1");
        friend1.setName("friend1");
        friend1.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend1);

        final User friend2 = new User();
        friend2.setEmail("friend2@mail.com");
        friend2.setLogin("friend2");
        friend2.setName("friend2");
        friend2.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend2);

        final User friend3 = new User();
        friend3.setEmail("friend3@mail.com");
        friend3.setLogin("friend3");
        friend3.setName("friend3");
        friend3.setBirthday(LocalDate.of(1991,6,15));

        userService.getUserStorage().create(friend3);

        userService.addFriend(user.getId(), friend1.getId());
        userService.addFriend(user.getId(), friend2.getId());
        userService.addFriend(friend3.getId(), friend2.getId());

        List<User> commonFriends = userService.commonFriends(user.getId(), friend3.getId());

        assertFalse(commonFriends.isEmpty());
        assertEquals(1, commonFriends.size());
        assertEquals(friend2.getEmail(), commonFriends.get(0).getEmail());
    }
}