package com.rockstars.musiclibrary.service;

import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.model.Artist;
import com.rockstars.musiclibrary.repository.ArtistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Cacheable("getAllArtists")
    public Page<Artist> findAll(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    public Optional<Artist> create(ArtistDTO artistDTO) {
        var artist = Artist.builder()
                .name(artistDTO.getName())
                .build();
        return Optional.of(artistRepository.save(artist));
    }

    @Cacheable(value = "artists", key = "#id")
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    public void update(Artist artist) {
        artistRepository.save(artist);
    }

}