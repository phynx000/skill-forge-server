package com.skillforge.skillforge_api.utils.strategy;

import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class DebitCardPaymentStrategy implements PaymentStrategy {


    @Override
    public PaymentDTO processPayment(PaymentRequestDTO request) {

        try {
            // Validate debit card info
            validateDebitCardInfo(request.getDebitCardInfo());

            // Simulate payment processing with bank gateway
            boolean paymentSuccess = processWithBankGateway(request);

            return PaymentDTO.builder()
                    .transactionId(generateTransactionId())
                    .status(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                    .paymentMethod(PaymentMethod.DEBIT_CARD)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .createdAt(LocalDateTime.now())
                    .message(paymentSuccess ? "Payment successful" : "Payment failed")
                    .build();

        } catch (Exception e) {
            return PaymentDTO.builder()
                    .transactionId(generateTransactionId())
                    .status(PaymentStatus.FAILED)
                    .paymentMethod(PaymentMethod.DEBIT_CARD)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .createdAt(LocalDateTime.now())
                    .message("Payment processing error: " + e.getMessage())
                    .build();
        }
    }

    private void validateDebitCardInfo(PaymentRequestDTO.DebitCardInfo cardInfo) {
        if (cardInfo == null) {
            throw new IllegalArgumentException("Debit card information is required");
        }
        // Additional validation logic
    }

    private boolean processWithBankGateway(PaymentRequestDTO request) {
        // Integrate with actual bank gateway
        // This is a simulation
        return ThreadLocalRandom.current().nextBoolean();
    }


    private String generateTransactionId() {
        return "EW_" + System.currentTimeMillis();
    }
    private void validateEWalletInfo(PaymentRequestDTO.EWalletInfo eWalletInfo) {
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.DEBIT_CARD;
    }


}
