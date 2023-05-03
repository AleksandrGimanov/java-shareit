package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(long id);

}
