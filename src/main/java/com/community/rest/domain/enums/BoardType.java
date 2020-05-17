package com.community.rest.domain.enums;

public enum BoardType {
    notice("Notice"),
    free("FreeBoard");

    private String value;

    BoardType(String value) {
    }

    public String getValue() {
        return this.value;
    }

}
