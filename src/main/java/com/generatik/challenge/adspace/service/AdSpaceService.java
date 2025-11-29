package com.generatik.challenge.adspace.service;

import com.generatik.challenge.adspace.model.AdSpaceEntity;
import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.model.AdSpaceType;
import com.generatik.challenge.adspace.repo.AdSpaceRepository;
import com.generatik.challenge.booking.repo.BookingRequestRepository;
import com.generatik.challenge.common.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.generatik.challenge.adspace.api.dto.CreateAdSpaceRequestDto;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdSpaceService {

    private final AdSpaceRepository repo;
    private final BookingRequestRepository bookingRepo;

    public AdSpaceService(AdSpaceRepository repo, BookingRequestRepository bookingRepo) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
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

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Ad space not found: " + id);
        }

        // Recommended: preserve booking history + avoid FK issues
        if (bookingRepo.existsByAdSpace_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete ad space because booking requests exist for it"
            );
        }

        repo.deleteById(id);
    }
}
