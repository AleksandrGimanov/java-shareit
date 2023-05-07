package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {

    private long generateId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++generateId);
        users.put(user.getId(), user);
        log.info("Пользователь с  id: {} создан", user.getId());
        return user;
    }

    @Override
    public User getUserById(long id) {
        checkId(id);
        log.info("Инфо о пользователе с  id: {} получено", id);
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Инфо о пользователях получена");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        checkId(user.getId());
        User oldUser = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        log.info("Пользователь с  id: {} обновлен", user.getId());
        return oldUser;
    }

    @Override
    public void deleteUser(long id) {
        checkId(id);
        users.remove(id);
        log.info("Пользователь с  id: {} удален", id);
    }

    private void checkId(Long id) {
        if (!users.containsKey(id)) {
            log.info("Пользователь с  id: {} не найден", id);
            throw new NotFoundException("Пользователь с id " +
                    id + " не найден!");
        }
    }


}
