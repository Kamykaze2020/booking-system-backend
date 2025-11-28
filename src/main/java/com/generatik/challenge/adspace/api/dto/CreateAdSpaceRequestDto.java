package com.generatik.challenge.adspace.api.dto;

import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.model.AdSpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateAdSpaceRequestDto(
        @NotBlank String name,
        @NotNull AdSpaceType type,
        @NotBlank String city,
        @NotBlank String address,
        @NotNull @Positive BigDecimal pricePerDay,
        // keep explicit for now; frontend can send AVAILABLE
        @NotNull AdSpaceStatus status
) {}
