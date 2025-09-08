package com.cms.inventory.controller;

import com.cms.inventory.dto.TheatreRequest;
import com.cms.inventory.dto.TheatreResponse;
import com.cms.inventory.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public ResponseEntity<List<TheatreResponse>> get() {
        return ResponseEntity.ok(theatreService.get());
    }

    @PostMapping
    public ResponseEntity<TheatreResponse> create(@RequestBody TheatreRequest request) {
        return ResponseEntity.ok(theatreService.create(request));
    }
}
