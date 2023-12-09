package com.rockstars.musiclibrary.repository;

import com.rockstars.musiclibrary.model.Song;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO songs(id, name, y, artist, shortname, bpm, duration, genre, spotifyid, album) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", nativeQuery = true)
    void insert(Long id,
                String name,
                Integer year,
                String artist,
                String shortName,
                Integer bpm,
                Long duration,
                String genre,
                String spotityId,
                String album);

}