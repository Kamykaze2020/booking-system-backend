package com.generatik.challenge.booking.api.dto;

import com.generatik.challenge.adspace.api.dto.AdSpaceDto;
import com.generatik.challenge.booking.model.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingDto(
        Long id,
        AdSpaceDto adSpace,
        String advertiserName,
        String advertiserEmail,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        BigDecimal totalCost,
        Instant createdAt
) {}
