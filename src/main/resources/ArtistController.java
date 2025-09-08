package com.rockstars.musiclibrary.controller;

import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.exception.MusicLibraryException;
import com.rockstars.musiclibrary.mappers.ArtistMapper;
import com.rockstars.musiclibrary.service.ArtistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class ArtistController {

    private final ArtistMapper artistMapper;
    private final ArtistService artistService;

    @GetMapping(value = "/api/artists", produces = "application/vnd.musiclibrary.v1+json")
    public ResponseEntity<Page<ArtistDTO>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(artistService.findAll(PageRequest.of(page, size))
            .map(artist -> modelMapper.map(artist, ArtistDTO.class)));
    }

    @GetMapping(value = "/api/artists", produces = "application/vnd.musiclibrary.v2+json")
    public ResponseEntity<Page<ArtistDTO>> getAllV2(@RequestParam(required = false) String name,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(artistService.findAll(PageRequest.of(page, size))
                .map(artist -> modelMapper.map(artist, ArtistDTO.class)));
    }


    @PostMapping(value = "/api/artists", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ArtistDTO> create(@Valid @RequestBody ArtistDTO artistDTO) {
        return artistService.create(artistDTO)
                .map(artist -> modelMapper.map(artist, ArtistDTO.class))
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
            .orElseThrow(() -> new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error while creating artist"));
    }

    @GetMapping(value = "/api/artists/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ArtistDTO> retrieve(@PathVariable Long id) {
        return artistService.findById(id)
            .map(artist -> modelMapper.map(artist, ArtistDTO.class))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping(value = "/api/artists", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ArtistDTO> update(@Valid @RequestBody ArtistDTO artistDTO) {
        return artistService.findById(artistDTO.getId())
            .map(artist -> ResponseEntity.ok(modelMapper.map(artistService.update(artist, artistDTO), ArtistDTO.class)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping(value = "/api/artists/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {return artistService.findById(id)
        .map(artist -> {
            artistService.delete(artist);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}