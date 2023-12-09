package com.rockstars.musiclibrary.repository;

import com.rockstars.musiclibrary.model.Artist;
import com.rockstars.musiclibrary.model.SequencesUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query(value = "SELECT nextval('" + SequencesUtils.ARTIST_SEQUENCE_NAME + "')", nativeQuery = true)
    public Long getSequenceNextValue();
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO artists(id, name) values(?, ?)", nativeQuery = true)
    void insert(Long id, String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM artists", nativeQuery = true)
    void deleteAll();
}