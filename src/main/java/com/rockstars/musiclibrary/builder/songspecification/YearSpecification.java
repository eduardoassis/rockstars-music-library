package com.rockstars.musiclibrary.builder.songspecification;

import com.rockstars.musiclibrary.model.Song;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class YearSpecification {

    private YearSpecification(){}

    public static Specification<Song> getSpecification(Specification<Song> specification, Integer year) {
        if (Objects.nonNull(year) && year > 0) {
            return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("year"), year));
        }
        return specification;
    }
}
