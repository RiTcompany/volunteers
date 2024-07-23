package org.example.enums;

import lombok.Getter;

@Getter
public enum EClothingSize {
    XXS("XXS"),
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL");

    private final String eClothingSizeStr;

    EClothingSize(String eClothingSize) {
        this.eClothingSizeStr = eClothingSize;
    }
}
