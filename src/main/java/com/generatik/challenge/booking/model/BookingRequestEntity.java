package com.generatik.challenge.booking.model;

import com.generatik.challenge.adspace.model.AdSpaceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(
        name = "booking_request",
        indexes = {
                @Index(name = "idx_booking_status", columnList = "status"),
                @Index(name = "idx_booking_space_dates", columnList = "ad_space_id,start_date,end_date")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class BookingRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ad_space_id", nullable = false)
    private AdSpaceEntity adSpace;

    @NotBlank
    @Column(nullable = false, name = "advertiser_name")
    private String advertiserName;

    @NotBlank
    @Email
    @Column(nullable = false, name = "advertiser_email")
    private String advertiserEmail;

    @NotNull
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BookingStatus status = BookingStatus.PENDING;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2, name = "total_cost")
    private BigDecimal totalCost;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;
}
