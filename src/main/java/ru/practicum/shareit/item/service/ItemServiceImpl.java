package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public Item createItem(Item item, long userId) {
        userStorage.getUserById(userId);
        return itemStorage.createItem(item, userId);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        if (getItemById(itemId).getOwner() != userId) {
            throw new NotFoundException("Пользователь не найден");
        }
        return itemStorage.updateItem(item, userId, itemId);
    }

    @Override
    public void deleteItem(long itemId) {
        itemStorage.deleteItem(itemId);
    }

    @Override
    public List<Item> getItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.getItemsByText(text);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        userStorage.getUserById(userId);
        return itemStorage.getAllItemsByUserId(userId);
    }
}
