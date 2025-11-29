package com.generatik.challenge.booking.api;

import com.generatik.challenge.IntegrationTestBase;
import com.generatik.challenge.adspace.model.*;
import com.generatik.challenge.adspace.repo.AdSpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class BookingControllerIT extends IntegrationTestBase {

  @Autowired MockMvc mvc;
  @Autowired AdSpaceRepository adSpaceRepo;

  @Test
  void createBooking_success_201() throws Exception {
    var s = new AdSpaceEntity();
    s.setName("Times Square");
    s.setType(AdSpaceType.BILLBOARD);
    s.setCity("NYC");
    s.setAddress("Manhattan");
    s.setPricePerDay(new BigDecimal("100.00"));
    s.setStatus(AdSpaceStatus.AVAILABLE);
    s = adSpaceRepo.save(s);

    var start = LocalDate.now().plusDays(10);
    var end = start.plusDays(7);

    mvc.perform(post("/api/v1/booking-requests")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"adSpaceId":%d,"advertiserName":"Acme","advertiserEmail":"test@acme.com","startDate":"%s","endDate":"%s"}
        """.formatted(s.getId(), start, end)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value("PENDING"))
      .andExpect(jsonPath("$.totalCost").value(700.00));
  }

  @Test
  void createBooking_validation_400() throws Exception {
    mvc.perform(post("/api/v1/booking-requests")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"adSpaceId":null,"advertiserName":"","advertiserEmail":"nope","startDate":null,"endDate":null}
        """))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Validation failed"));
  }
}
