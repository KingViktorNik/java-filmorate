package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {


    private final UserService userService;
    private final FriendService friendService;
    private User user;

    @BeforeEach
    void addUser() {
        user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        user = userService.create(user).get();
    }

    @Test
    void create() {
        long userId = userService.create(user).get().getId();

        assertFalse(userService.userAll().isEmpty());
        assertEquals("Jimi", userService.getUserById(userId).get().getName());
    }

    @Test
    void createNullName() {
        user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name(null)
                .birthday(LocalDate.of(1990,5,5))
                .build();

        long userId = userService.create(user).get().getId();

        assertFalse(userService.userAll().isEmpty());
        assertEquals("Jim878", userService.getUserById(userId).get().getName());
    }

    @Test
    void createBlankName() {
        User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("    ")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        user = userService.create(user).get();

        assertFalse(userService.userAll().isEmpty());
        assertEquals("Jim878", userService.getUserById(user.getId()).get().getName());
    }

    @Test
    void createNullLogin() {
        final User user = User.builder()
                .email("jimi@mail.com")
                .login(null)
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("login не корректный.",exception.getMessage());
    }

    @Test
    void createBlankLogin() {
        final User user = User.builder()
                .email("jimi@mail.com")
                .login("      ")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("login не корректный.",exception.getMessage());
    }

    @Test
    void createNullEmail() {
        final User user = User.builder()
                .email(null)
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("email не корректный.",exception.getMessage());
    }

    @Test
    void createBlankEmail() {
        final User user = User.builder()
            .email("             ")
            .login("Jim878")
            .name("Jimi")
            .birthday(LocalDate.of(1990,5,5))
            .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("email не корректный.",exception.getMessage());
    }

    @Test
    void createIncorrectEmail() {
        final User user = User.builder()
                .email("jimimailcom")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("email не корректный.",exception.getMessage());
    }

    @Test
    void createIncorrectBirthday() {
        final User user = User.builder()
                        .email("jimi@mail.com")
                        .login("Jim878")
                        .name("Jimi")
                        .birthday(LocalDate.of(2023,5,5))
                        .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> userService.create(user)
        );
        assertEquals("Дата не может быть в будущем.",exception.getMessage());
    }

    @Test
    void update() {
        final User user = User.builder()
                        .email("jimi@mail.com")
                        .login("Jim878")
                        .name("Jimi")
                        .birthday(LocalDate.of(1990,5,5))
                        .build();

        userService.create(user);

        final User userNew = User.builder()
                        .id(1)
                        .email("jimi@mail.com")
                        .login("JimTheWorm")
                        .name(null)
                        .birthday(LocalDate.of(1990,5,5))
                        .build();

        userService.update(userNew);

        assertNotEquals(user, userService.userAll().get(0));
        assertEquals("JimTheWorm", userService.userAll().get(0).getName());
    }

    @Test
    void updateException() {
        final User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();

        userService.create(user);
        user.setId(555);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> userService.update(user)
        );
        assertEquals("Пользователя с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userIncorrectId() {
        final User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();
        userService.create(user);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> userService.getUserById(555)
        );
        assertEquals("Пользователя с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userId() {
        user = userService.create(user).get();
        User user2 = userService.getUserById(user.getId()).get();
        assertEquals(user, user2);
    }

    @Test
    void nullUserId() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> userService.getUserById(555L)
        );
        assertEquals("Пользователя с id: '555' не существует!",exception.getMessage());

    }

    @Test
    void userAll() {
        List<User> userAll = userService.userAll();
        assertNotEquals(0, userAll.size());
    }

    @Test
    void addHappyFriend() {
        User friend = User.builder()
                .email("bobic@mail.com")
                .login("Bond")
                .name("Bob")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend = userService.create(friend).get();

        user = friendService.addFriend(user.getId(), friend.getId()).get();

        List<Long> userList = new ArrayList<>(userService.getUserById(user.getId()).get().getFriends());
        List<Long> friendList = new ArrayList<>(userService.getUserById(friend.getId()).get().getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.size(), 0);
    }

    @Test
    void addFriendNullUser() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> friendService.addFriend(555L,user.getId())
        );
        assertEquals("Пользователь с id:555 не существует.",exception.getMessage());
    }

    @Test
    void addFriendNullFriend() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> friendService.addFriend(user.getId(), 555L)
        );
        assertEquals("Пользователь с id:555 не существует.",exception.getMessage());
    }

    @Test
    void addYourselfAsFriends() {
        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> friendService.addFriend(user.getId(), user.getId())
        );
        assertEquals("Пользователь не может добавить сам себя в друзья!",exception.getMessage());
    }

    @Test
    void deleteHappyFriend() {
        User friend = User.builder()
                .email("bobic@mail.com")
                .login("Bond")
                .name("Bob")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend = userService.create(friend).get();

        user = friendService.addFriend(user.getId(), friend.getId()).get();

        List<Long> userList = new ArrayList<>(user.getFriends());
        List<Long> friendList = new ArrayList<>(friend.getFriends());

        assertEquals(userList.get(0), friend.getId());
        assertEquals(friendList.size(), 0);

        user = friendService.deleteFriend(user.getId(), friend.getId()).get();

        assertTrue(user.getFriends().isEmpty());
        assertTrue(userService.getUserById(friend.getId()).get().getFriends().isEmpty());
    }

    @Test
    void deleteFriendNullUser() {
        User friend = User.builder()
                .email("bobic@mail.com")
                .login("Bond")
                .name("Bob")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        long friendId = userService.create(friend).get().getId();

        friendService.addFriend(user.getId(), friendId);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> friendService.deleteFriend(555L, friendId)
        );
        assertEquals("Пользователь с id:555 не существует.",exception.getMessage());
    }

    @Test
    void deleteFriendNullFriend() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> friendService.deleteFriend(user.getId(), 555L)
        );
        assertEquals("Пользователь с id:555 не существует.",exception.getMessage());
    }

    @Test
    void listFriends() {
        User friend = User.builder()
                .email("bobic@mail.com")
                .login("Bond")
                .name("Bob")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend = userService.create(friend).get();

        user = friendService.addFriend(user.getId(), friend.getId()).get();
        List<User> users = friendService.allFriends(user.getId());
        assertEquals("bobic@mail.com", users.get(0).getEmail());
    }

    @Test
    void commonFriends() {
        User friend1 = User.builder()
                .email("friend1@mail.com")
                .login("friend1")
                .name("friend1")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend1 = userService.create(friend1).get();

        User friend2 = User.builder()
                .email("friend2@mail.com")
                .login("friend2")
                .name("friend2")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend2 = userService.create(friend2).get();

        User friend3 = User.builder()
                .email("friend3@mail.com")
                .login("friend3")
                .name("friend3")
                .birthday(LocalDate.of(1991,6,15))
                .build();

        friend3 = userService.create(friend3).get();

        friendService.addFriend(user.getId(), friend1.getId());
        user = friendService.addFriend(user.getId(), friend2.getId()).get();
        friend3 = friendService.addFriend(friend3.getId(), friend2.getId()).get();

        List<User> commonFriends = friendService.commonFriends(user.getId(), friend3.getId());

        assertFalse(commonFriends.isEmpty());
        assertEquals(1, commonFriends.size());
        assertEquals(friend2.getEmail(), commonFriends.get(0).getEmail());
    }
}