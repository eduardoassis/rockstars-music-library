package com.rockstars.musiclibrary.model;

import com.rockstars.musiclibrary.dto.SongDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song implements Serializable {

    @SequenceGenerator(initialValue = 2622, allocationSize = 1, name = SequencesUtils.SONG_SEQUENCE_NAME, sequenceName = SequencesUtils.SONG_SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequencesUtils.SONG_SEQUENCE_NAME)
    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "y")
    private Integer year;

    @Column(name = "artist", nullable = false)
    private String artist;
    @Column(name = "shortname")
    private String shortName;
    private Integer bpm;
    private Long duration;
    private String genre;
    @Column(name = "spotifyid")
    private String spotifyId;
    private String album;

    public void update(SongDTO songDTO) {
        this.setName(songDTO.getName());
        this.setBpm(songDTO.getBpm());
        this.setYear(songDTO.getYear());
        this.setArtist(songDTO.getArtist());
        this.setShortName(songDTO.getShortName());
        this.setDuration(songDTO.getDuration());
        this.setGenre(songDTO.getArtist());
        this.setSpotifyId(songDTO.getSpotifyId());
        this.setAlbum(songDTO.getAlbum());
    }
}