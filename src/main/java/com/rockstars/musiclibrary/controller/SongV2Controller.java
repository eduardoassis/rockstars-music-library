package com.rockstars.musiclibrary.controller;


import com.rockstars.musiclibrary.dto.PageDTO;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.mappers.SongMapper;
import com.rockstars.musiclibrary.model.YearQueryParam;
import com.rockstars.musiclibrary.service.SongService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class SongV2Controller {

    private final SongMapper songMapper;
    private final SongService songService;

    @GetMapping(value = "/api/songs", produces = "application/vnd.musiclibrary.v2+json")
    public ResponseEntity<PageDTO<SongDTO>> getAllV2(@RequestParam(required = false) String genre,
                                                     @RequestParam(required = false) Integer year,
                                                     @RequestParam(required = false, defaultValue = "BEFORE_INCL") YearQueryParam comparative,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(songService.findAll(genre, year, comparative, PageRequest.of(page, size)));
    }

}