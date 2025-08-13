package com.skillforge.skillforge_api.utils.constant;

public enum PaymentMethod {
    DEBIT_CARD("DEBIT_CARD", "Thẻ ghi nợ"),
    E_WALLET("E_WALLET", "Ví điện tử");

    private final String code;
    private final String displayName;

    PaymentMethod(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
}
