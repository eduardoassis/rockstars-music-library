package com.rockstars.musiclibrary.model;

import com.rockstars.musiclibrary.dto.ArtistDTO;
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
@Table(name = "artists")
public class Artist implements Serializable {

    @Id
    @SequenceGenerator(initialValue = 902, allocationSize = 1, name = SequencesUtils.ARTIST_SEQUENCE_NAME, sequenceName = SequencesUtils.ARTIST_SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequencesUtils.ARTIST_SEQUENCE_NAME)
    @Column(updatable = false, unique = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public void update(ArtistDTO dto) {
        this.name = dto.getName();
    }
}