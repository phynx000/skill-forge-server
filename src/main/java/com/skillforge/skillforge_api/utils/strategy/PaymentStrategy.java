package com.skillforge.skillforge_api.entity;

import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;

public interface PaymentStrategy {
    PaymentDTO processPayment(PaymentRequestDTO request);
    PaymentMethod getSupportedMethod();
}
