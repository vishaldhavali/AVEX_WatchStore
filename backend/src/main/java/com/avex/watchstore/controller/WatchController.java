package com.avex.watchstore.controller;

import com.avex.watchstore.model.Watch;
import com.avex.watchstore.service.WatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watches")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @GetMapping
    public ResponseEntity<List<Watch>> getAllWatches(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean featured) {
        return ResponseEntity.ok(watchService.getAllWatches(category, featured));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Watch> getWatchById(@PathVariable Long id) {
        return ResponseEntity.ok(watchService.getWatchById(id));
    }

    @PostMapping
    public ResponseEntity<Watch> createWatch(@Valid @RequestBody Watch watch) {
        return ResponseEntity.status(HttpStatus.CREATED).body(watchService.createWatch(watch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Watch> updateWatch(@PathVariable Long id, @Valid @RequestBody Watch watch) {
        return ResponseEntity.ok(watchService.updateWatch(id, watch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatch(@PathVariable Long id) {
        watchService.deleteWatch(id);
        return ResponseEntity.noContent().build();
    }
}
