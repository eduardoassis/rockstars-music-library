package com.rockstars.musiclibrary.api;

import com.rockstars.musiclibrary.dto.ArtistDTO;
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

import java.util.List;

public interface ArtistApi {

    @GetMapping(
            value = "/api/artists",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<Page<ArtistDTO>> getAll(@RequestParam(required = false) String name,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size);

    @PostMapping(
            value = "/api/artists",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ArtistDTO> create(@Valid @RequestBody ArtistDTO artistDTO);


    @GetMapping(
            value = "/api/artists/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<ArtistDTO> retrieve(@PathVariable Long id);

    @PutMapping(
            value = "/api/artists",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ArtistDTO> update(@Valid @RequestBody ArtistDTO artistDTO);

    @DeleteMapping(
            value = "/api/artists/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<Object> delete(@PathVariable Long id);

}