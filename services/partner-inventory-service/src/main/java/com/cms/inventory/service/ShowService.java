package com.cms.inventory.service;

import com.cms.inventory.dto.ShowRequest;
import com.cms.inventory.dto.ShowResponse;
import com.cms.inventory.repository.ScreenRepository;
import com.cms.inventory.repository.ShowRepository;
import com.cms.shared.model.ScreenEntity;
import com.cms.shared.model.ShowEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowService {

    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;

    public List<ShowResponse> findShowsByCityAndDate(String city, Long showId, LocalDateTime start, LocalDateTime end) {
        return showRepository
                .findByIdAndScreen_Theatre_CityAndStartAtGreaterThanEqualAndStartAtLessThan(showId, city.trim(), start, end)
                .stream().map(ShowResponse::fromEntity).toList();
    }

    public ShowResponse create(ShowRequest request) {
        Optional<ScreenEntity> screenEntity = screenRepository.findById(request.screenId());
        if (screenEntity.isEmpty()) throw new IllegalArgumentException("Invalid screen");
        ShowEntity entity = new ShowEntity();
        entity.setStartAt(request.startAt());
        entity.setEndAt(request.endAt());
        entity.setScreen(screenEntity.get());
        entity = showRepository.save(entity);
        return ShowResponse.fromEntity(entity);
    }
}
