package com.rockstars.musiclibrary.builder.songspecification;

import com.rockstars.musiclibrary.model.Song;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class GenreSpecification {

    private GenreSpecification(){}
    public static Specification<Song> getSpecification(Specification<Song> specification, String genre) {
        if (StringUtils.isNotBlank(genre)) {
            return specification.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get("genre"), genre);
            });
        }
        return specification;
    }
}
