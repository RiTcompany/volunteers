package org.example.enums;

import lombok.Getter;

@Getter
public enum EAgreement {
    OK("ОК"),
    CLEARLY("Понятно"),;

    private final String agreementStr;

    EAgreement(String agreementStr) {
        this.agreementStr = agreementStr;
    }
}
