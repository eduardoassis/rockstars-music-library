package com.rockstars.musiclibrary.api;

import com.rockstars.musiclibrary.dto.SongDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface SongApi {

    @GetMapping(
            value = "/api/songs",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<Page<SongDTO>> getAll(@RequestParam(required = false) String genre,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size);

    @PostMapping(
            value = "/api/songs",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<SongDTO> create(@Valid @RequestBody SongDTO songDTO);

    @GetMapping(
            value = "/api/songs/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<SongDTO> retrieve(@PathVariable Long id);

    @PutMapping(
            value = "/api/songs",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<SongDTO> update(@Valid @RequestBody SongDTO songDTO);

    @DeleteMapping(
            value = "/api/songs/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<Object> delete(@PathVariable Long id);

}
