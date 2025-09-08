package com.rockstars.musiclibrary.service;

import com.rockstars.musiclibrary.builder.songspecification.SongSpecificationBuilder;
import com.rockstars.musiclibrary.dto.PageDTO;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.mappers.SongMapper;
import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.model.YearQueryParam;
import com.rockstars.musiclibrary.repository.SongRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SongService {

    private SongRepository songRepository;
    private SongMapper songMapper;

    @Cacheable(value = "songs", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PageDTO<SongDTO> findAll(Pageable pageable) {
        log.info("Finding all songs");
        var page = songRepository.findAll(pageable);
        var dtos = page.getContent()
                .stream()
                .map(songMapper::toDTO)
                .toList();
        return new PageDTO<>(dtos, page.getTotalElements(), page.getPageable().getPageNumber(), page.getPageable().getPageSize());
    }

    @Cacheable(value = "songs", key = "#genre + '-' + #year + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public PageDTO<SongDTO> findAll(String genre, Integer year, YearQueryParam comparative, Pageable pageable) {
        log.info("Finding all songs for genre {} and year {}", genre, year);
        var spec = new SongSpecificationBuilder.Builder()
            .genre(genre)
            .year(year, comparative)
            .build();
        var page = songRepository.findAll(spec, pageable);
        var dtos = page.getContent()
                .stream()
                .map(songMapper::toDTO)
                .toList();
        return new PageDTO<>(dtos, page.getTotalElements(), page.getPageable().getPageNumber(), page.getPageable().getPageSize());
    }

    @CacheEvict(value = "songs", allEntries = true)
    public Optional<Song> create(SongDTO songDTO) {
        log.info("Creating song {}", songDTO);
        var song = Song.
        builder()
            .bpm(songDTO.getBpm())
            .name(songDTO.getName())
            .year(songDTO.getYear())
            .artist(songDTO.getArtist())
            .shortName(songDTO.getShortName())
            .duration(songDTO.getDuration())
            .genre(songDTO.getGenre())
            .spotifyId(songDTO.getSpotifyId())
            .album(songDTO.getAlbum())
        .build();
        return Optional.of(songRepository.save(song));
    }

    @Cacheable(value = "songs", key = "#id")
    public Optional<Song> findById(Long id) {
        log.info("Finding song {}", id);
        return songRepository.findById(id);
    }

    @CacheEvict(value = "songs", allEntries = true)
    public void update(Song song, SongDTO songDTO) {
        log.info("Updating song {}", song);
        song.update(songDTO);
        songRepository.save(song);
    }

    @Caching(evict = {
        @CacheEvict(value = "songs", allEntries = true),
        @CacheEvict(value = "songs", key = "#song.id")
    })
    public void delete(Song song) {
        log.info("Deleting song {}", song);
        songRepository.delete(song);
    }
}