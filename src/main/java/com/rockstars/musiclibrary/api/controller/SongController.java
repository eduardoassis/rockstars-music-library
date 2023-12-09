package com.rockstars.musiclibrary.api.controller;


import com.rockstars.musiclibrary.api.SongApi;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.exception.MusicLibraryException;
import com.rockstars.musiclibrary.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
public class SongController implements SongApi {

    private final ModelMapper modelMapper;
    private final SongService songService;

    @Override
    public ResponseEntity<Page<SongDTO>> getAll(String genre, Integer year, int page, int size) {
        return ResponseEntity.ok(songService
                .findAll(genre, year, PageRequest.of(page, size))
                .map(song -> modelMapper.map(song, SongDTO.class)));
    }

    @Override
    public ResponseEntity<SongDTO> create(@Valid @RequestBody SongDTO songDTO) {
        try {
            var dto = songService.create(songDTO)
                    .map(song -> modelMapper.map(song, SongDTO.class))
                    .orElseThrow();
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception ex) {
            throw new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error creating song");
        }
    }

    @Override
    public ResponseEntity<SongDTO> retrieve(Long id) {
        return songService.findById(id)
                .map(song -> ResponseEntity.ok(modelMapper.map(song, SongDTO.class)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<SongDTO> update(SongDTO songDTO) {
        try {
            return songService.findById(songDTO.getId())
                    .map(song -> {
                        song.setName(songDTO.getName());
                        song.setBpm(songDTO.getBpm());
                        song.setYear(songDTO.getYear());
                        song.setArtist(songDTO.getArtist());
                        song.setShortName(songDTO.getShortName());
                        song.setDuration(songDTO.getDuration());
                        song.setGenre(songDTO.getArtist());
                        song.setSpotifyId(songDTO.getSpotifyId());
                        song.setAlbum(songDTO.getAlbum());
                        songService.update(song);
                        return ResponseEntity.ok(modelMapper.map(song, SongDTO.class));
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            throw new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error creating song");
        }
    }

    @Override
    public ResponseEntity<Object> delete(Long id) {
        return songService.findById(id)
                .map(song -> {
                    song.setName(song.getName());
                    songService.update(song);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}