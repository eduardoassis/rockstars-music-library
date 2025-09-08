package com.rockstars.musiclibrary.builder.songspecification;

import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.model.YearQueryParam;
import org.springframework.data.jpa.domain.Specification;

public class SongSpecificationBuilder {

    public static class Builder {

        Specification<Song> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        public Builder genre(String genre) {
            specification = GenreSpecification.getSpecification(specification, genre);
            return this;
        }

        public Builder year(Integer year, YearQueryParam comparative) {
            specification = YearSpecification.getSpecification(specification, year, comparative);
            return this;
        }

        public Specification<Song> build() {
            return specification;
        }

        public Builder comparative() {
            return null;
        }
    }
}