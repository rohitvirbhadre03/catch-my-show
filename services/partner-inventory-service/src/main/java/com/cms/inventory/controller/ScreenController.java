package com.cms.inventory.controller;

import com.cms.inventory.dto.ScreenRequest;
import com.cms.inventory.dto.ScreenResponse;
import com.cms.inventory.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screens")
public class ScreenController {

    private final ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<ScreenResponse>> get() {
        return ResponseEntity.ok(screenService.get());
    }

    @GetMapping("/{screenId}")
    public ResponseEntity<ScreenResponse> get(@PathVariable Long screenId) {
        return ResponseEntity.ok(screenService.getScreen(screenId));
    }

    @PostMapping
    public ResponseEntity<ScreenResponse> create(@RequestBody ScreenRequest request) {
        return ResponseEntity.ok(screenService.create(request));
    }
}
