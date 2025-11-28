package com.generatik.challenge.booking.api;

import com.generatik.challenge.booking.api.dto.BookingDto;
import com.generatik.challenge.booking.api.dto.CreateBookingRequestDto;
import com.generatik.challenge.booking.model.BookingStatus;
import com.generatik.challenge.booking.repo.BookingRequestRepository;
import com.generatik.challenge.booking.service.BookingService;
import com.generatik.challenge.common.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking-requests")
public class BookingController {

    private final BookingService service;
    private final BookingRequestRepository repo;

    public BookingController(BookingService service, BookingRequestRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@Valid @RequestBody CreateBookingRequestDto req) {
        return BookingMapper.toDto(service.create(req));
    }

    @GetMapping("/{id}")
    public BookingDto get(@PathVariable Long id) {
        return repo.findById(id).map(BookingMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + id));
    }

    @GetMapping
    public List<BookingDto> list(@RequestParam(required = false) BookingStatus status) {
        var list = (status == null) ? repo.findAll() : repo.findByStatus(status);
        return list.stream().map(BookingMapper::toDto).toList();
    }

    @PatchMapping("/{id}/approve")
    public BookingDto approve(@PathVariable Long id) {
        return BookingMapper.toDto(service.approve(id));
    }

    @PatchMapping("/{id}/reject")
    public BookingDto reject(@PathVariable Long id) {
        return BookingMapper.toDto(service.reject(id));
    }
}
