package com.skillforge.skillforge_api.dto.response;

import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentDTO {
    private String transactionId;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
    private String message;
    private String paymentUrl; // Cho trường hợp redirect đến gateway
}
