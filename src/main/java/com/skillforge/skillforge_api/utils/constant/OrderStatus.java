package com.skillforge.skillforge_api.utils.constant;

public enum OrderStatus
{
    PENDING("Đang chờ xử lý"),
    PROCESSING("Đang xử lý"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy"),
    FAILED("Thất bại");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
