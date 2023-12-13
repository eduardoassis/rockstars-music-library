package com.rockstars.musiclibrary.builder.songspecification;

import com.rockstars.musiclibrary.model.Song;
import com.rockstars.musiclibrary.model.YearQueryParam;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class YearSpecification {

    private YearSpecification(){}

    public static Specification<Song> getSpecification(Specification<Song> specification, Integer year, YearQueryParam comparative) {
        if (Objects.nonNull(year) && year > 0) {
            return switch (comparative) {
                case BEFORE -> specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("year"), year));
                case BEFORE_INCL -> specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("year"), year));
                case EQUALS_TO -> specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("year"), year));
                case AFTER_THAN -> specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("year"), year));
                case AFTER_THAN_INCL -> specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), year));
            };
        }
        return specification;
    }
}