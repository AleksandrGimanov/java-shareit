package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.bookingRepository.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public Item createItem(Item item, long userId) {
        userService.getUserIfExistOrThrow(userId);
        item.setOwner(userId);
        log.info("Пользователь с id: {} добавил предмет с id: {}", userId, item.getId());
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(long itemId, long userId) {
        Item item = getItemIfExistOrThrow(itemId);
        item.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
        List<Booking> bookings = bookingRepository.findByItemId(itemId);
        if (bookings.isEmpty() || item.getOwner() != userId) {
            return item;
        }
        item.setLastBooking(bookingMapper.bookingShortDtoToItem(getLastBooking(bookings)));
        item.setNextBooking(bookingMapper.bookingShortDtoToItem(getNextBooking(bookings)));
        return item;
    }

    @Override
    public Item updateItem(Item item, long userId, long itemId) {
        userService.getUserIfExistOrThrow(userId);
        throwIfItemNotOwnedUser(itemId, userId);
        Item oldItem = itemRepository.getReferenceById(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getOwner() != null && item.getOwner() > 0) {
            oldItem.setOwner(item.getOwner());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        Item updatedItem = itemRepository.save(oldItem);
        log.info("Пользователь с id: {} обновил предмет с id: {}", userId, itemId);
        return updatedItem;
    }

    @Override
    public void deleteItem(long itemId) {
        getItemIfExistOrThrow(itemId);
        itemRepository.deleteById(itemId);
        log.info("Предмет с  id: {} удален ", itemId);
    }

    @Override
    public List<Item> getItemsByText(String text) {
        if (text.isEmpty())
            return List.of();
        return itemRepository.findItemsByText(text);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        userService.getUserIfExistOrThrow(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        for (Item item : items) {
            List<Booking> bookings = bookingRepository.findByItemId(item.getId());
            if (bookings.isEmpty()) {
                continue;
            }
            item.setLastBooking(bookingMapper.bookingShortDtoToItem(getLastBooking(bookings)));
            item.setNextBooking(bookingMapper.bookingShortDtoToItem(getNextBooking(bookings)));
        }
        return items;
    }

    @Override
    public Item getItemIfExistOrThrow(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        });
    }

    private void throwIfItemNotOwnedUser(long itemId, long userId) {
        if (getItemIfExistOrThrow(itemId).getOwner() != userId)
            throw new NotFoundException("Вещь с id " + itemId + " не найдена у пользователя с id " + userId);
    }

    @Override
    public Comment createComment(long userId, long itemId, Comment comment) {
        if (comment.getText().isBlank()) {
            throw new ValidationException("Text is empty");
        }
        List<Long> bookings = bookingRepository.findByItemIdAndEndIsBefore(itemId, LocalDateTime.now()).stream()
                .map(booking -> booking.getBooker().getId())
                .collect(Collectors.toList());
        if (!bookings.contains(userId)) {
            throw new ValidationException("User did not rent this item");
        }
        User user = userService.getUserIfExistOrThrow(userId);
        Item item = getItemIfExistOrThrow(itemId);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    private Booking getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()) &&
                        b.getStatus().equals(BookingStatus.APPROVED))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }

    private Booking getLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()) &&
                        b.getStatus().equals(BookingStatus.APPROVED))
                .max(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }
}
