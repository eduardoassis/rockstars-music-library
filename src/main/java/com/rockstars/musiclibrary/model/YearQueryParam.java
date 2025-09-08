package com.rockstars.musiclibrary.model;

import lombok.Getter;

@Getter
public enum YearQueryParam {
    BEFORE("BEFORE"),
    BEFORE_INCL("BEFORE_INCL"),
    EQUALS_TO("EQUALS_TO"),
    AFTER_THAN("AFTER_THAN"),
    AFTER_THAN_INCL("AFTER_THAN_INCL");

    private final String value;

    YearQueryParam(String value) {
        this.value = value;
    }

}