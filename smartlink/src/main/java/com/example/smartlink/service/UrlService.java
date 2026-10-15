package com.example.smartlink.service;

import com.example.smartlink.model.Url;
import com.example.smartlink.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // CREATE SHORT URL
    public Url createShortUrl(Url url) {

        // Check whether original URL already exists
        Optional<Url> existingUrl =
                urlRepository.findByOriginalUrl(url.getOriginalUrl());

        // If it exists, return the existing record
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate new short code
        String shortCode = generateUniqueShortCode();

        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());

        return urlRepository.save(url);
    }

    // GET ALL URLs
    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    // GET URL BY ID
    public Url getUrlById(Long id) {

        return urlRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("URL not found with ID: " + id));
    }

    // FIND ORIGINAL URL USING SHORT CODE
    public Url getUrlByShortCode(String shortCode) {

        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found"));
    }

    // DELETE URL
    public void deleteUrl(Long id) {

        Url url = urlRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("URL not found with ID: " + id));

        urlRepository.delete(url);
    }

    // GENERATE UNIQUE SHORT CODE
    private String generateUniqueShortCode() {

        String shortCode;

        do {
            shortCode = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 6);

        } while (urlRepository.findByShortCode(shortCode).isPresent());

        return shortCode;
    }
}