package com.rockstars.musiclibrary.service;

import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.model.Artist;
import com.rockstars.musiclibrary.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class ArtistService {

    private ArtistRepository artistRepository;

    @Cacheable(value = "artists", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Artist> findAll(Pageable pageable) {
        log.info("Finding all artists");
        return artistRepository.findAll(pageable);
    }

    @CacheEvict(value = "artists", allEntries = true)
    public Optional<Artist> create(ArtistDTO artistDTO) {
        log.info("Creating artist {}", artistDTO);
        var artist = Artist.builder()
                .name(artistDTO.getName())
                .build();
        return Optional.of(artistRepository.save(artist));
    }

    @Cacheable(value = "artists", key = "#id")
    public Optional<Artist> findById(Long id) {
        log.info("Finding artist {}", id);
        return artistRepository.findById(id);
    }

    @CacheEvict(value = "artists", allEntries = true)
    public Artist update(Artist artist, ArtistDTO artistDTO) {
        log.info("Updating artist {}", artist);
        artist.update(artistDTO);
        return artistRepository.save(artist);
    }

    @CacheEvict(value = "artists", allEntries = true)
    public void delete(Artist artist) {
        log.info("Deleting artist {}", artist);
        artistRepository.delete(artist);
    }
}