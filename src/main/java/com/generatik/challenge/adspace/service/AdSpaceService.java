package com.generatik.challenge.adspace.service;

import com.generatik.challenge.adspace.model.AdSpaceEntity;
import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.model.AdSpaceType;
import com.generatik.challenge.adspace.repo.AdSpaceRepository;
import com.generatik.challenge.common.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.generatik.challenge.adspace.api.dto.CreateAdSpaceRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdSpaceService {

    private final AdSpaceRepository repo;

    public AdSpaceService(AdSpaceRepository repo) {
        this.repo = repo;
    }

    public List<AdSpaceEntity> listAvailable(AdSpaceType type, String city) {
        var status = AdSpaceStatus.AVAILABLE;

        if (type != null && city != null && !city.isBlank()) {
            return repo.findByStatusAndTypeAndCity(status, type, city);
        }
        if (type != null) {
            return repo.findByStatusAndType(status, type);
        }
        if (city != null && !city.isBlank()) {
            return repo.findByStatusAndCity(status, city);
        }
        return repo.findByStatus(status);
    }

    public AdSpaceEntity getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Ad space not found: " + id));
    }

    @Transactional
    public AdSpaceEntity create(CreateAdSpaceRequestDto req) {
        var e = new AdSpaceEntity();
        e.setName(req.name());
        e.setType(req.type());
        e.setCity(req.city());
        e.setAddress(req.address());
        e.setPricePerDay(req.pricePerDay());
        e.setStatus(req.status());
        return repo.save(e);
    }
}

