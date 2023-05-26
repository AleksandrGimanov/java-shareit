package ru.practicum.shareit.booking.bookingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking as b where b.booker.id = :userId and b.status = :state order by b.start desc")
    List<Booking> findByBookerIdAndState(long userId, BookingStatus state);

    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking as b where b.item.owner = :userId and b.status = :state order by b.start desc")
    List<Booking> findByItemOwnerAndState(long userId, BookingStatus state);

    List<Booking> findByItemOwnerOrderByStartDesc(long userId);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByItemIdAndEndIsBefore(Long itemId, LocalDateTime end);
}
