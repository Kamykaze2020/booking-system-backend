package com.generatik.challenge.adspace.api;

import com.generatik.challenge.adspace.api.dto.AdSpaceDetailsDto;
import com.generatik.challenge.adspace.api.dto.AdSpaceDto;
import com.generatik.challenge.adspace.model.AdSpaceEntity;

public final class AdSpaceMapper {
    private AdSpaceMapper() {}

    public static AdSpaceDto toDto(AdSpaceEntity e) {
        return new AdSpaceDto(e.getId(), e.getName(), e.getType(), e.getCity(), e.getPricePerDay(), e.getStatus());
    }

    public static AdSpaceDetailsDto toDetailsDto(AdSpaceEntity e) {
        return new AdSpaceDetailsDto(e.getId(), e.getName(), e.getType(), e.getCity(), e.getAddress(), e.getPricePerDay(), e.getStatus());
    }
}

