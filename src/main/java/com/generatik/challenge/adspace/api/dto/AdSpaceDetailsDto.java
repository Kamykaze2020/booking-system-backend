package com.generatik.challenge.adspace.api.dto;

import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.model.AdSpaceType;

import java.math.BigDecimal;

public record AdSpaceDetailsDto(
        Long id,
        String name,
        AdSpaceType type,
        String city,
        String address,
        BigDecimal pricePerDay,
        AdSpaceStatus status
) {}

