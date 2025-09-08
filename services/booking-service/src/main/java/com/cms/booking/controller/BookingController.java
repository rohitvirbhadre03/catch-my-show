package com.cms.booking.controller;

import com.cms.booking.dto.BookingCreateRequest;
import com.cms.booking.dto.BookingResponse;
import com.cms.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> get() {
        return ResponseEntity.ok(bookingService.get());
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request) {
        var response = bookingService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/bookings/" + response.id())).body(response);
    }
}
