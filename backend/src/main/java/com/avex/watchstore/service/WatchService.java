package com.avex.watchstore.service;

import com.avex.watchstore.model.Watch;
import com.avex.watchstore.model.Watch.WatchCategory;
import com.avex.watchstore.repository.WatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WatchService {

    private final WatchRepository watchRepository;

    public List<Watch> getAllWatches(String category, Boolean featured) {
        if (category != null && featured != null) {
            WatchCategory cat = WatchCategory.valueOf(category.toUpperCase());
            return watchRepository.findByCategoryAndFeatured(cat, featured);
        }
        if (category != null) {
            WatchCategory cat = WatchCategory.valueOf(category.toUpperCase());
            return watchRepository.findByCategory(cat);
        }
        if (featured != null) {
            return watchRepository.findByFeatured(featured);
        }
        return watchRepository.findAll();
    }

    public Watch getWatchById(Long id) {
        return watchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watch not found with id: " + id));
    }

    @Transactional
    public Watch createWatch(Watch watch) {
        return watchRepository.save(watch);
    }

    @Transactional
    public Watch updateWatch(Long id, Watch watchDetails) {
        Watch watch = getWatchById(id);
        watch.setName(watchDetails.getName());
        watch.setBrand(watchDetails.getBrand());
        watch.setDescription(watchDetails.getDescription());
        watch.setPrice(watchDetails.getPrice());
        watch.setImageUrl(watchDetails.getImageUrl());
        watch.setCategory(watchDetails.getCategory());
        watch.setStockQuantity(watchDetails.getStockQuantity());
        watch.setFeatured(watchDetails.isFeatured());
        return watchRepository.save(watch);
    }

    @Transactional
    public void deleteWatch(Long id) {
        Watch watch = getWatchById(id);
        watchRepository.delete(watch);
    }
}
