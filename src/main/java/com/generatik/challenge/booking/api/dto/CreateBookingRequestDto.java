package com.generatik.challenge.booking.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequestDto(
        @NotNull Long adSpaceId,
        @NotBlank String advertiserName,
        @NotBlank @Email String advertiserEmail,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
