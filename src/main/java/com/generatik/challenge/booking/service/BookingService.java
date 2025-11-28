package com.generatik.challenge.booking.service;

import com.generatik.challenge.adspace.model.AdSpaceEntity;
import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.repo.AdSpaceRepository;
import com.generatik.challenge.booking.api.dto.CreateBookingRequestDto;
import com.generatik.challenge.booking.model.BookingRequestEntity;
import com.generatik.challenge.booking.model.BookingStatus;
import com.generatik.challenge.booking.repo.BookingRequestRepository;
import com.generatik.challenge.common.error.BusinessException;
import com.generatik.challenge.common.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class BookingService {

    private final BookingRequestRepository bookingRepo;
    private final AdSpaceRepository adSpaceRepo;

    public BookingService(BookingRequestRepository bookingRepo, AdSpaceRepository adSpaceRepo) {
        this.bookingRepo = bookingRepo;
        this.adSpaceRepo = adSpaceRepo;
    }

    public BookingRequestEntity create(CreateBookingRequestDto req) {
        AdSpaceEntity space = adSpaceRepo.findById(req.adSpaceId())
                .orElseThrow(() -> new NotFoundException("Ad space not found: " + req.adSpaceId()));

        validateCreate(space, req.startDate(), req.endDate());

        long days = ChronoUnit.DAYS.between(req.startDate(), req.endDate());
        BigDecimal total = space.getPricePerDay().multiply(BigDecimal.valueOf(days));

        BookingRequestEntity br = new BookingRequestEntity();
        br.setAdSpace(space);
        br.setAdvertiserName(req.advertiserName());
        br.setAdvertiserEmail(req.advertiserEmail());
        br.setStartDate(req.startDate());
        br.setEndDate(req.endDate());
        br.setStatus(BookingStatus.PENDING);
        br.setTotalCost(total);

        return bookingRepo.save(br);
    }

    public BookingRequestEntity approve(Long bookingId) {
        BookingRequestEntity br = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (br.getStatus() != BookingStatus.PENDING) {
            throw new BusinessException("Only PENDING bookings can be approved");
        }

        // ensure space still eligible
        AdSpaceEntity space = br.getAdSpace();
        if (space.getStatus() != AdSpaceStatus.AVAILABLE) {
            throw new BusinessException("Cannot approve: ad space is not AVAILABLE");
        }

        if (bookingRepo.existsApprovedOverlap(space.getId(), br.getStartDate(), br.getEndDate())) {
            throw new BusinessException("Cannot approve: overlapping APPROVED booking exists");
        }

        br.setStatus(BookingStatus.APPROVED);

        // Simple interpretation of "reflect unavailability for that period":
        // mark as BOOKED if the approved booking overlaps today.
        LocalDate today = LocalDate.now();
        if (!today.isBefore(br.getStartDate()) && today.isBefore(br.getEndDate())) {
            space.setStatus(AdSpaceStatus.BOOKED);
        }

        return br;
    }

    public BookingRequestEntity reject(Long bookingId) {
        BookingRequestEntity br = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + bookingId));

        if (br.getStatus() != BookingStatus.PENDING) {
            throw new BusinessException("Only PENDING bookings can be rejected");
        }

        br.setStatus(BookingStatus.REJECTED);
        return br;
    }

    private void validateCreate(AdSpaceEntity space, LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();

        if (!start.isAfter(today)) {
            throw new BusinessException("Start date must be in the future");
        }
        if (!end.isAfter(start)) {
            throw new BusinessException("End date must be after start date");
        }

        long days = ChronoUnit.DAYS.between(start, end);
        if (days < 7) {
            throw new BusinessException("Minimum booking duration is 7 days");
        }

        if (space.getStatus() != AdSpaceStatus.AVAILABLE) {
            throw new BusinessException("Cannot book: ad space is not AVAILABLE");
        }

        if (bookingRepo.existsApprovedOverlap(space.getId(), start, end)) {
            throw new BusinessException("Cannot book: overlapping APPROVED booking exists");
        }
    }
}
