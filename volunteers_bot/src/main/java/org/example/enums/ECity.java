package org.example.enums;

import lombok.Getter;

@Getter
public enum ECity {
    SPB("Санкт-Петербург"),
    OTHER("Другое");

    private final String cityStr;

    ECity(String cityStr) {
        this.cityStr = cityStr;
    }
}
