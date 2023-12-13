package com.rockstars.musiclibrary.service;

import com.rockstars.musiclibrary.builder.songspecification.SongSpecificationBuilder;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.model.YearQueryParam;
import com.rockstars.musiclibrary.repository.SongRepository;
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
public class SongService {

    private SongRepository songRepository;

    @Cacheable(value = "songs")
    public Page<Song> findAll(String genre, Integer year, YearQueryParam comparative, Pageable pageable) {
        var spec = new SongSpecificationBuilder.Builder()
            .genre(genre)
            .year(year, comparative)
            .build();
        return songRepository.findAll(spec, pageable);
    }

    public Optional<Song> create(SongDTO songDTO) {
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
        return songRepository.findById(id);
    }

    public void update(Song song) {
        songRepository.save(song);
    }

}