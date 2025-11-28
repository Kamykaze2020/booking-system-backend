package com.generatik.challenge.booking.api;

import com.generatik.challenge.adspace.api.AdSpaceMapper;
import com.generatik.challenge.booking.api.dto.BookingDto;
import com.generatik.challenge.booking.model.BookingRequestEntity;

public final class BookingMapper {
    private BookingMapper() {}

    public static BookingDto toDto(BookingRequestEntity br) {
        return new BookingDto(
                br.getId(),
                AdSpaceMapper.toDto(br.getAdSpace()),
                br.getAdvertiserName(),
                br.getAdvertiserEmail(),
                br.getStartDate(),
                br.getEndDate(),
                br.getStatus(),
                br.getTotalCost(),
                br.getCreatedAt()
        );
    }
}

