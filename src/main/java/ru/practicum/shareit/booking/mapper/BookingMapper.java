package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDtoToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@Component
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto) {
        Item item = new Item();
        item.setId(bookingDto.getItemId());
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(bookingDto.getBooker())
                .status(bookingDto.getStatus())
                .build();
    }

    public BookingShortDtoToItem bookingShortDtoToItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingShortDtoToItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
