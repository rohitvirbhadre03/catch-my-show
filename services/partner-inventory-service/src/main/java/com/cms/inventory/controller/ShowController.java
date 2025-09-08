package com.cms.inventory.controller;

import com.cms.inventory.dto.ShowRequest;
import com.cms.inventory.dto.ShowResponse;
import com.cms.inventory.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final ShowService showService;

    @GetMapping
    public ResponseEntity<List<ShowResponse>> get(
            @RequestParam String city,
            @RequestParam Long showId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        var start = date.atStartOfDay();
        var end   = start.plusDays(1);
        return ResponseEntity.ok(showService.findShowsByCityAndDate(city.trim(), showId, start, end));
    }

    @PostMapping
    public ResponseEntity<ShowResponse> create(@RequestBody ShowRequest request) {
        return ResponseEntity.ok(showService.create(request));
    }
}
