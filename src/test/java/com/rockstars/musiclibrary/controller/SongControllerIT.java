package com.rockstars.musiclibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockstars.musiclibrary.AbstractIntegrationTest;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.exception.ErrorMessage;
import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class SongControllerIT extends AbstractIntegrationTest {

    private static final String BASE_URL_SONGS = "/api/songs";

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void initDataBase() {
        var song = Song.builder()
            .album("Acústico MTV 2 - Gafieira")
            .artist("Zeca Pagodinho")
            .name("Quando A Gira Girou")
            .genre("Samba e Pagode")
            .year(2006)
        .build();

        var song2 = Song.builder()
                .album("Passado e Presente")
                .artist("Nelson Gonçalves")
                .name("Naquela mesa ")
                .genre("MPB, Samba e Pagode")
                .year(1974)
                .build();

        var song3 = Song.builder()
                .name("Verdade")
                .artist("Zeca Pagodinho")
                .album("Deixa Clarear")
                .genre("Samba e Pagode")
        .build();
        songRepository.save(song);
        songRepository.save(song2);
        songRepository.save(song3);
    }

    @Test
    void shouldGetAllSongs() throws Exception {
        mvc.perform(get(BASE_URL_SONGS)
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetOnlyMetalSongs() throws Exception {
        MvcResult result = mvc.perform(get(BASE_URL_SONGS)
                        .param("page", "0")
                        .param("size", "10")
                        .param("genre", "Metal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/vnd.musiclibrary.v2+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(259))
                .andReturn();
    }

    @Test
    void shouldGetMetalSongsBefore2016() throws Exception {
        mvc.perform(get(BASE_URL_SONGS)
                        .param("page", "0")
                        .param("size", "10")
                        .param("year", "2016")
                        .param("genre", "Metal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/vnd.musiclibrary.v2+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(257));
    }

    @Test
    void shouldGetMetalSongsBefore2023() throws Exception {
        mvc.perform(get(BASE_URL_SONGS)
                        .param("page", "0")
                        .param("size", "10")
                        .param("year", "2023")
                        .param("genre", "Metal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/vnd.musiclibrary.v2+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(259));
    }

    @Test
    void shouldCreate() throws Exception {
        var song = SongDTO.
        builder()
            .bpm(121)
            .name("Aqualung")
            .year(1971)
            .artist("Jethro Tull")
            .shortName("aqualung")
            .duration(398690L)
            .genre("Prog")
            .spotifyId("5UuikgHTxSRFRnC0zXx10i")
            .album("Aqualung")
        .build();
        mvc.perform(post(BASE_URL_SONGS)
                        .content(objectMapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateWithAnEmptyName() throws Exception {
        var song = SongDTO.
                builder()
                .bpm(121)
                .year(1971)
                .artist("Jethro Tull")
                .shortName("aqualung")
                .duration(398690L)
                .genre("Prog")
                .spotifyId("5UuikgHTxSRFRnC0zXx10i")
                .album("Aqualung")
                .build();
        var result = mvc.perform(post("/api/songs")
                        .content(objectMapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessages());
        assertTrue(errorMessage.getMessages().contains("Song name is required"));
    }


    @Test
    void shouldNotCreateWithAnEmptyArtistName() throws Exception {
        var song = SongDTO.
                builder()
                .bpm(121)
                .year(1971)
                .name("Aqualung")
                .shortName("aqualung")
                .duration(398690L)
                .genre("Prog")
                .spotifyId("5UuikgHTxSRFRnC0zXx10i")
                .album("Aqualung")
                .build();
        var result = mvc.perform(post("/api/songs")
                        .content(objectMapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessages());
        assertTrue(errorMessage.getMessages().contains("Artist name is required"));
    }


    @Test
    void shouldRetrieveSongById() throws Exception {
        var song = songRepository.findOne(Example.of(Song.builder().artist("Zeca Pagodinho").name("Verdade").build())).get();
        var result = mvc.perform(get("/api/songs/" + song.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
        var retrievedSong = objectMapper.readValue(result.getResponse().getContentAsString(), SongDTO.class);
        assertNotNull(retrievedSong);
        assertEquals(song.getName(), retrievedSong.getName());
        assertEquals(song.getArtist(), retrievedSong.getArtist());
        assertEquals(song.getYear(), retrievedSong.getYear());
        assertEquals(song.getGenre(), retrievedSong.getGenre());
    }

    @Test
    void shouldNotRetrieveSongArtistById() throws Exception {
        var id = songRepository.count();
        mvc.perform(get("/api/songs/" + id + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingSong() throws Exception {
        var song = songRepository.findOne(Example.of(Song.builder().artist("Zeca Pagodinho").name("Quando A Gira Girou").build())).get();
        var dto = modelMapper.map(song, SongDTO.class);
        dto.setArtist("Zeca Pagodinho de Iraja RJ");
        mvc.perform(put("/api/songs")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateWithAnEmptyName() throws Exception {
        var song = songRepository.findOne(Example.of(Song.builder().artist("Zeca Pagodinho").name("Verdade").build())).get();
        var dto = modelMapper.map(song, SongDTO.class);
        dto.setName("");
        var result = mvc.perform(put("/api/songs")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessages());
        assertTrue(errorMessage.getMessages().contains("Song name is required"));
    }

    @Test
    void shouldDelete() throws Exception {
        var song = songRepository.findOne(Example.of( Song.builder()
                .album("Passado e Presente")
                .artist("Nelson Gonçalves")
                .name("Naquela mesa ")
                .genre("MPB, Samba e Pagode").build())).get();

        mvc.perform(delete("/api/songs/" + song.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
    }

}