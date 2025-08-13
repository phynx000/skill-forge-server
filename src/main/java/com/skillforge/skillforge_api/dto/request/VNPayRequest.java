package com.skillforge.skillforge_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRequest {
    private BigDecimal amount;
    private String orderInfo;
    private String orderType;
    private String language;
    private String bankCode;
    private String clientIp;

    // Additional fields for our system
    private String transactionId;
    private String currency;
}
