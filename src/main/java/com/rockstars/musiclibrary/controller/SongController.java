package com.rockstars.musiclibrary.controller;


import com.rockstars.musiclibrary.dto.PageDTO;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.exception.MusicLibraryException;
import com.rockstars.musiclibrary.mappers.SongMapper;
import com.rockstars.musiclibrary.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
public class SongController {

    private final SongMapper songMapper;
    private final SongService songService;

    @GetMapping(value = "/api/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageDTO<SongDTO>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(songService
                .findAll(PageRequest.of(page, size)));
    }

    @PostMapping(
            value = "/api/songs",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SongDTO> create(@Valid @RequestBody SongDTO songDTO) {
        try {
            return songService.create(songDTO)
                .map(songMapper::toDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElseThrow();
        } catch (Exception ex) {
            throw new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error creating song");
        }
    }

    @GetMapping(value = "/api/songs/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongDTO> retrieve(@PathVariable Long id) {
        return songService.findById(id)
                .map(songMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping(value = "/api/songs", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SongDTO> update(@Valid @RequestBody SongDTO songDTO) {
        try {
            return songService.findById(songDTO.getId())
                    .map(song -> {
                        songService.update(song, songDTO);
                        return ResponseEntity.ok(songMapper.toDTO(song));
                    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            throw new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error creating song");
        }
    }

    @DeleteMapping(value = "/api/songs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return songService.findById(id)
                .map(song -> {
                    song.setName(song.getName());
                    songService.delete(song);
                    return ResponseEntity.noContent().<Void>build();
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}