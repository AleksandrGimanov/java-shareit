package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, long userId);

    Booking getBookingById(long bookingId, long userId);

    Booking approvedBooking(long userId, long bookingId, boolean approved);

    List<Booking> getAllBookingsByBookerIdAndState(long userId, String state);

    List<Booking> getAllBookingsByOwnerIdAndState(long userId, String state);
}