package com.generatik.challenge.booking.service;

import com.generatik.challenge.adspace.model.*;
import com.generatik.challenge.adspace.repo.AdSpaceRepository;
import com.generatik.challenge.booking.api.dto.CreateBookingRequestDto;
import com.generatik.challenge.booking.model.BookingRequestEntity;
import com.generatik.challenge.booking.model.BookingStatus;
import com.generatik.challenge.booking.repo.BookingRequestRepository;
import com.generatik.challenge.common.error.BusinessException;
import com.generatik.challenge.common.error.NotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

  BookingRequestRepository bookingRepo = mock(BookingRequestRepository.class);
  AdSpaceRepository adSpaceRepo = mock(AdSpaceRepository.class);
  BookingService service = new BookingService(bookingRepo, adSpaceRepo);

  @Test
  void create_rejectsStartNotFuture() {
    var space = adSpace(1L, AdSpaceStatus.AVAILABLE, new BigDecimal("10.00"));
    when(adSpaceRepo.findById(1L)).thenReturn(Optional.of(space));

    LocalDate start = LocalDate.now(); // not future
    LocalDate end = start.plusDays(7);

    var req = new CreateBookingRequestDto(1L, "A", "a@b.com", start, end);
    var ex = assertThrows(BusinessException.class, () -> service.create(req));
    assertTrue(ex.getMessage().contains("future"));
  }

  @Test
  void create_rejectsMinDuration() {
    var space = adSpace(1L, AdSpaceStatus.AVAILABLE, new BigDecimal("10.00"));
    when(adSpaceRepo.findById(1L)).thenReturn(Optional.of(space));

    LocalDate start = LocalDate.now().plusDays(10);
    LocalDate end = start.plusDays(6); // < 7

    var req = new CreateBookingRequestDto(1L, "A", "a@b.com", start, end);
    var ex = assertThrows(BusinessException.class, () -> service.create(req));
    assertTrue(ex.getMessage().contains("Minimum booking duration"));
  }

  @Test
  void create_calculatesTotalCost() {
    var space = adSpace(1L, AdSpaceStatus.AVAILABLE, new BigDecimal("100.00"));
    when(adSpaceRepo.findById(1L)).thenReturn(Optional.of(space));
    when(bookingRepo.existsApprovedOverlap(anyLong(), any(), any())).thenReturn(false);
    when(bookingRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    LocalDate start = LocalDate.now().plusDays(10);
    LocalDate end = start.plusDays(7);

    var req = new CreateBookingRequestDto(1L, "A", "a@b.com", start, end);
    BookingRequestEntity br = service.create(req);

    assertEquals(BookingStatus.PENDING, br.getStatus());
    assertEquals(new BigDecimal("700.00"), br.getTotalCost());
  }

  @Test
  void approve_onlyPending() {
    var br = new BookingRequestEntity();
    br.setStatus(BookingStatus.REJECTED);

    when(bookingRepo.findById(99L)).thenReturn(Optional.of(br));
    var ex = assertThrows(BusinessException.class, () -> service.approve(99L));
    assertTrue(ex.getMessage().contains("Only PENDING"));
  }

  @Test
  void create_throwsNotFoundIfSpaceMissing() {
    when(adSpaceRepo.findById(1L)).thenReturn(Optional.empty());
    var req = new CreateBookingRequestDto(1L, "A", "a@b.com",
      LocalDate.now().plusDays(10), LocalDate.now().plusDays(17));
    assertThrows(NotFoundException.class, () -> service.create(req));
  }

  private static AdSpaceEntity adSpace(Long id, AdSpaceStatus status, BigDecimal price) {
    var s = new AdSpaceEntity();
    s.setId(id);
    s.setName("Test");
    s.setType(AdSpaceType.BILLBOARD);
    s.setCity("X");
    s.setAddress("Y");
    s.setStatus(status);
    s.setPricePerDay(price);
    return s;
  }
}

