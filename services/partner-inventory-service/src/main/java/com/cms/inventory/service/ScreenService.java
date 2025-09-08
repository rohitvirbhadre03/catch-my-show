package com.cms.inventory.service;

import com.cms.inventory.dto.ScreenRequest;
import com.cms.inventory.dto.ScreenResponse;
import com.cms.inventory.repository.ScreenRepository;
import com.cms.inventory.repository.TheatreRepository;
import com.cms.shared.model.ScreenEntity;
import com.cms.shared.model.TheatreEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScreenService {

    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "screensAll", key = "'all'")
    public List<ScreenResponse> get() {
        return screenRepository.findAll()
                .stream()
                .map(ScreenResponse::fromEntity)
                .toList();
    }

    @Cacheable(cacheNames = "screenSeatLayout", key = "#screenId")
    public ScreenResponse getScreen(Long screenId) {
        var entity = screenRepository.findById(screenId)
                .orElseThrow(() -> new NoSuchElementException("Screen not found: " + screenId));
        return ScreenResponse.fromEntity(entity);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "screensAll", key = "'all'"),
            @CacheEvict(cacheNames = "screenSeatLayout", key = "#result.id") // works if method returns ScreenResponse/Entity
    })
    public ScreenResponse create(ScreenRequest request) {
        Optional<TheatreEntity> theatreEntity = theatreRepository.findByPartnerIdAndId(request.partnerId(), request.theatreId());
        if (theatreEntity.isEmpty()) throw new IllegalArgumentException("Invalid theatre ID for the given tenant");

        ScreenEntity entity = new ScreenEntity();
        entity.setTheatre(theatreEntity.get());
        entity.setSeatLayout(request.seatLayout());
        entity.setName(request.name());
        screenRepository.save(entity);
        return ScreenResponse.fromEntity(entity);
    }
}
