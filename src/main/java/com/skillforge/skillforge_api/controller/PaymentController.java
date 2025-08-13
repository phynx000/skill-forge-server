package com.skillforge.skillforge_api.controller;


import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.service.PaymentService;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.skillforge.skillforge_api.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Validated
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentRequestDTO request) {
        try {
            PaymentDTO response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment request", e);
            return ResponseEntity.badRequest().body(
                    PaymentDTO.builder()
                            .status(PaymentStatus.FAILED)
                            .message(e.getMessage())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        } catch (Exception e) {
            log.error("Payment processing failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    PaymentDTO.builder()
                            .status(PaymentStatus.FAILED)
                            .message("Internal server error")
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        }
    }


    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethod>> getSupportedPaymentMethods() {
        return ResponseEntity.ok(paymentService.getSupportedPaymentMethods());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        // Implementation for getting payment details
        return ResponseEntity.ok().build();
    }


}
