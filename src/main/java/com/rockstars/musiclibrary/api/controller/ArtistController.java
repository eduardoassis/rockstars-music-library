package com.rockstars.musiclibrary.api.controller;

import com.rockstars.musiclibrary.api.ArtistApi;
import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.exception.MusicLibraryException;
import com.rockstars.musiclibrary.service.ArtistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
public class ArtistController implements ArtistApi {

    private final ModelMapper modelMapper;
    private final ArtistService artistService;

    @Override
    public ResponseEntity<Page<ArtistDTO>> getAll(@RequestParam(required = false) String name,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(artistService
                .findAll(PageRequest.of(page, size))
                .map(artist -> modelMapper.map(artist, ArtistDTO.class)));
    }

    @Override
    public ResponseEntity<ArtistDTO> create(@Valid ArtistDTO artistDTO) {
        return artistService.create(artistDTO)
            .map(artist -> ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(artist, ArtistDTO.class)))
            .orElseThrow(() -> new MusicLibraryException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), "Error while creating artist"));
    }

    @Override
    public ResponseEntity<ArtistDTO> retrieve(Long id) {
        return artistService.findById(id)
            .map(artist -> ResponseEntity.ok(modelMapper.map(artist, ArtistDTO.class)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @Override
    public ResponseEntity<ArtistDTO> update(@Valid ArtistDTO artistDTO) {
        return artistService.findById(artistDTO.getId())
            .map(artist -> ResponseEntity.ok(modelMapper.map(artistService.update(artist, artistDTO), ArtistDTO.class)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Object> delete(Long id) {
        return artistService.findById(id)
            .map(artist -> {
                artistService.delete(artist);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}