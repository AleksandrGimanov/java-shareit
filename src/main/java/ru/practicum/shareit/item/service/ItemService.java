package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(Item item, long userId);

    Item getItemById(long itemId, long userId);

    Item updateItem(Item item, long userId, long itemId);

    void deleteItem(long itemId);

    List<Item> getItemsByText(String text);

    List<Item> getAllItemsByUserId(long userId);

    Item getItemIfExistOrThrow(long itemId);

    Comment createComment(long userId, long itemId, Comment comment);

}
