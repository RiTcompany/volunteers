package org.example.enums;

import lombok.Getter;

@Getter
public enum ECity {
    SPB("Санкт-Петербург"),
    NOT_SPB("Другое");

    private final String cityName;

    ECity(String cityName) {
        this.cityName = cityName;
    }
}
