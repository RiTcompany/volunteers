package org.example.enums;

import lombok.Getter;

@Getter
public enum EAgreement {
    YES("ДА");

    private final String agreementStr;

    EAgreement(String agreementStr) {
        this.agreementStr = agreementStr;
    }
}