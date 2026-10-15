package com.example.smartlink.controller;

import com.example.smartlink.model.Url;
import com.example.smartlink.service.UrlService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // CREATE SHORT URL
    @PostMapping
    public ResponseEntity<Url> createShortUrl(
            @RequestBody Url url) {

        Url savedUrl = urlService.createShortUrl(url);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUrl);
    }

    // GET ALL URLs
    @GetMapping
    public ResponseEntity<List<Url>> getAllUrls() {

        return ResponseEntity.ok(
                urlService.getAllUrls()
        );
    }

    // GET URL BY ID
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Url> getUrlById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                urlService.getUrlById(id)
        );
    }

    // REDIRECT USING SHORT CODE
    @GetMapping("/short/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode) {

        Url url = urlService.getUrlByShortCode(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", url.getOriginalUrl())
                .build();
    }

    // DELETE URL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable Long id) {

        urlService.deleteUrl(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
