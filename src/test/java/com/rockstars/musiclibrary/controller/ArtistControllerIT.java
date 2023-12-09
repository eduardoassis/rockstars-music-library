package com.rockstars.musiclibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockstars.musiclibrary.AbstractIntegrationTest;
import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.exception.ErrorMessage;
import com.rockstars.musiclibrary.model.Artist;
import com.rockstars.musiclibrary.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class ArtistControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void deleteAll() {
        artistRepository.deleteAll();
    }

    @Override
    public void initDataBase() {
        var name = "Zeca Pagodinho";
        var artist = ArtistDTO.builder().name(name).build();
        artistRepository.save(Artist.builder().name(artist.getName()).build());
    }

    @Test
    void shouldGetArtists() throws Exception {
        mvc.perform(get("/api/artists")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreate() throws Exception {
        var artist = ArtistDTO.builder().name("Cartola").build();
        mvc.perform(post("/api/artists")
                        .content(objectMapper.writeValueAsString(artist))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateWithAnEmptyName() throws Exception {
        var artist = ArtistDTO.builder().build();
        var result = mvc.perform(post("/api/artists")
                        .content(objectMapper.writeValueAsString(artist))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessages());
        assertTrue(errorMessage.getMessages().contains("Name is required"));
    }

    @Test
    void shouldNotCreateWithAnExistingName() throws Exception {
        var artist = ArtistDTO.builder().name("Zeca Pagodinho").build();
        var result = mvc.perform(post("/api/artists")
            .content(objectMapper.writeValueAsString(artist))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertEquals("The id and name cannot be duplicated", errorMessage.getMessage());
    }

    @Test
    void shouldRetrieveArtistById() throws Exception {
        var createdArtist = artistRepository.findOne(Example.of(Artist.builder().name("Zeca Pagodinho").build()));
        var result = mvc.perform(get("/api/artists/" + createdArtist.get().getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
        ArtistDTO retrievedArtist = objectMapper.readValue(result.getResponse().getContentAsString(), ArtistDTO.class);
        assertNotNull(retrievedArtist);
        assertEquals(createdArtist.get().getName(), retrievedArtist.getName());
    }

    @Test
    void shouldNotRetrieveArtistById() throws Exception {
        var id = artistRepository.count();
        mvc.perform(get("/api/artists/" + id + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingArtist() throws Exception {
        var createdArtist = artistRepository.findOne(Example.of(Artist.builder().name("Zeca Pagodinho").build()));
        var dto = ArtistDTO.builder()
            .id(createdArtist.get().getId())
            .name("Zeca Pagodinho de Iraja RJ")
            .build();
        mvc.perform(put("/api/artists")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateWithAnEmptyName() throws Exception {
        var createdArtist = artistRepository.findOne(Example.of(Artist.builder().name("Zeca Pagodinho").build()));
        var dto = ArtistDTO.builder()
                .id(createdArtist.get().getId())
                .build();
        var result = mvc.perform(put("/api/artists")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        var errorMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessages());
        assertTrue(errorMessage.getMessages().contains("Name is required"));
    }

    @Test
    void shouldDeleteArtist() throws Exception {
        var createdArtist = artistRepository.save(Artist.builder().name("Chico Buarque").build());
        mvc.perform(delete("/api/artists/" + createdArtist.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
    }

}