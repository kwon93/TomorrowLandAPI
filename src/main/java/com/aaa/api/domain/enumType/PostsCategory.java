package com.aaa.api.domain.enumType;

import lombok.Getter;

@Getter
public enum PostsCategory {

    LIFE("LIFE"),
    DEV("DEV"),
    MEDIA("MEDIA"),
    ETC("ETC");

    String category;

    PostsCategory(String category) {
        this.category = category;
    }

    public String value() {
        return category;
    }
}
