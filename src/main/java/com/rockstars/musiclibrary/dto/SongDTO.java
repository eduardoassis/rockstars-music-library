package com.rockstars.musiclibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SongDTO implements Serializable {
    @JsonProperty("Id")
    private Long id;

    @NotBlank(message = "Song name is required")
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Year")
    private Integer year;


    @NotBlank(message = "Artist name is required")
    @JsonProperty("Artist")
    private String artist;

    @JsonProperty("Shortname")
    private String shortName;
    @JsonProperty("Bpm")
    private Integer bpm;
    @JsonProperty("Duration")
    private Long duration;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("SpotifyId")
    private String spotifyId;
    @JsonProperty("Album")
    private String album;
}