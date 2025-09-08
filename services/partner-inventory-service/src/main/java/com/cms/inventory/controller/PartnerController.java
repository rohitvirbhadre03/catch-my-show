package com.cms.inventory.controller;

import com.cms.inventory.dto.PartnerRequest;
import com.cms.inventory.dto.PartnerResponse;
import com.cms.inventory.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partner")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> get() {
        return ResponseEntity.ok(partnerService.get());
    }

    @PostMapping
    public ResponseEntity<PartnerResponse> create(@RequestBody PartnerRequest request) {
        return ResponseEntity.ok(partnerService.create(request));
    }
}
