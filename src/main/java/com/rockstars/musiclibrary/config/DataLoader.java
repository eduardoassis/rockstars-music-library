package com.rockstars.musiclibrary.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockstars.musiclibrary.dto.ArtistDTO;
import com.rockstars.musiclibrary.dto.SongDTO;
import com.rockstars.musiclibrary.model.Artist;
import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.repository.ArtistRepository;
import com.rockstars.musiclibrary.repository.SongRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class DataLoader implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ArtistRepository artistRepository;

    private final SongRepository songRepository;

    @Value("classpath:data/artists.json")
    private Resource artistsFile;

    @Value("classpath:data/songs.json")
    private Resource songsFile;

    @Autowired
    public DataLoader(ModelMapper modelMapper, ObjectMapper objectMapper, ArtistRepository artistRepository, SongRepository songRepository) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
    }

    public void run(ApplicationArguments args) {
        try {
            loadArtists();
            loadSongs();
        } catch (IOException e) {
            log.error("Error while loading initial data.");
            throw new RuntimeException(e);
        }
    }

    private void loadArtists() throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("data/artists.json");
        try {
            byte[] binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            var strJson = new String(binaryData, StandardCharsets.UTF_8);
            if (artistRepository.count() == 0) {
                List<ArtistDTO> artistsDto = objectMapper.readValue(strJson, new TypeReference<List<ArtistDTO>>() {});
                artistsDto.stream()
                        .map(artistDTO -> modelMapper.map(artistDTO, Artist.class))
                        .forEach(artist -> artistRepository.insert(artist.getId(), artist.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadSongs() throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("data/songs.json");
        try {
            byte[] binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            var strJson = new String(binaryData, StandardCharsets.UTF_8);
            if (songRepository.count() == 0) {
                List<SongDTO> songsDTO = objectMapper.readValue(strJson, new TypeReference<List<SongDTO>>() {});
                songsDTO.stream()
                        .map(songDTO -> modelMapper.map(songDTO, Song.class))
                        .forEach(song -> songRepository.insert(song.getId(),
                                song.getName(),
                                song.getYear(),
                                song.getArtist(),
                                song.getShortName(),
                                song.getBpm(),
                                song.getDuration(),
                                song.getGenre(),
                                song.getSpotifyId(),
                                song.getAlbum()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}