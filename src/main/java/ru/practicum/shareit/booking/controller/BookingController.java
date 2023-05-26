package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingServiceImpl;
    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingServiceImpl.createBooking(bookingMapper.toBooking(bookingDto), userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approvedBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId,
                                   @RequestParam boolean approved) {
        return bookingServiceImpl.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        return bookingServiceImpl.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingServiceImpl.getAllBookingsByBookerIdAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsByItemsOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingServiceImpl.getAllBookingsByOwnerIdAndState(userId, state);
    }
}
