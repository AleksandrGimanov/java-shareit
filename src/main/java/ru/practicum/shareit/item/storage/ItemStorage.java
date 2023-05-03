package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemStorage {

    Item createItem(Item item, long userId);

    Item getItemById(long id);

    List<Item> getAllItemsByUserId(long userId);

    List<Item> getItemsByText(String text);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long id);

}
