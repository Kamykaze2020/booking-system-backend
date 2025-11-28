package com.generatik.challenge.booking.repo;

import com.generatik.challenge.booking.model.BookingRequestEntity;
import com.generatik.challenge.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRequestRepository extends JpaRepository<BookingRequestEntity, Long> {

    @Query("""
    select count(br) > 0 from BookingRequestEntity br
    where br.adSpace.id = :adSpaceId
      and br.status = com.generatik.challenge.booking.model.BookingStatus.APPROVED
      and br.startDate < :endDate
      and br.endDate > :startDate
  """)
    boolean existsApprovedOverlap(Long adSpaceId, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = "adSpace")
    List<BookingRequestEntity> findByStatus(BookingStatus status);

    @Override
    @EntityGraph(attributePaths = "adSpace")
    List<BookingRequestEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "adSpace")
    Optional<BookingRequestEntity> findById(Long id);
}
