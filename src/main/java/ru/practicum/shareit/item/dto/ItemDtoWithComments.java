package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDtoToItem;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoWithComments {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private BookingShortDtoToItem lastBooking;
    private BookingShortDtoToItem nextBooking;
    private List<CommentDto> comments;
}
