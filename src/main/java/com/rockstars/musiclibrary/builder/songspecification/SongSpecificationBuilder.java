package com.rockstars.musiclibrary.builder.songspecification;

import com.rockstars.musiclibrary.model.Song;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class SongSpecificationBuilder {

    public static class Builder {

        Specification<Song> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        public Builder genre(String genre) {
            specification = GenreSpecification.getSpecification(specification, genre);
            return this;
        }

        public Builder year(Integer year) {
            specification = YearSpecification.getSpecification(specification, year);
            return this;
        }

        public Specification<Song> build() {
            return specification;
        }
    }
}