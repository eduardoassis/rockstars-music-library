package com.rockstars.musiclibrary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArtistDTO {

    @JsonProperty("Id")
    private Long id;

    @NotBlank(message = "Name is required")
    @JsonProperty("Name")
    private String name;
}
