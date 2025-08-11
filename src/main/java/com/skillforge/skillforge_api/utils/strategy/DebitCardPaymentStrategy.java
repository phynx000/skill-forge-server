package com.skillforge.skillforge_api.utils.strategy;

import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;

import java.time.LocalDateTime;

public class EWalletPaymentStrategy implements PaymentStrategy {
    

    @Override
    public PaymentDTO processPayment(PaymentRequestDTO request) {

        try {
            // Validate e-wallet info
            validateEWalletInfo(request.getEWalletInfo());

            // Process based on wallet provider
            String paymentUrl = processWithWalletProvider(request);

            return PaymentDTO.builder()
                    .transactionId(generateTransactionId())
                    .status(PaymentStatus.PENDING) // E-wallet usually requires redirect
                    .paymentMethod(PaymentMethod.E_WALLET)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .createdAt(LocalDateTime.now())
                    .message("Redirect to wallet provider for payment")
                    .paymentUrl(paymentUrl)
                    .build();

        } catch (Exception e) {
            return PaymentDTO.builder()
                    .transactionId(generateTransactionId())
                    .status(PaymentStatus.FAILED)
                    .paymentMethod(PaymentMethod.E_WALLET)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .createdAt(LocalDateTime.now())
                    .message("Payment processing error: " + e.getMessage())
                    .build();
        }
    }

    private String processWithWalletProvider(PaymentDTO request) {
        // Integrate with specific wallet providers (MoMo, ZaloPay, VNPay, etc.)
        String provider = request.getEWalletInfo().getWalletProvider();

        switch (provider.toUpperCase()) {
            case "MOMO":
                return "https://test-payment.momo.vn/pay/" + generateTransactionId();
            case "ZALOPAY":
                return "https://sb-openapi.zalopay.vn/pay/" + generateTransactionId();
            case "VNPAY":
                return "https://sandbox.vnpayment.vn/pay/" + generateTransactionId();
            default:
                throw new IllegalArgumentException("Unsupported wallet provider: " + provider);
        }
    }

    private String generateTransactionId() {
        return "EW_" + System.currentTimeMillis();
    }
    private void validateEWalletInfo(PaymentRequestDTO.EWalletInfo eWalletInfo) {
    }

    @Override
    public PaymentMethod getSupportedMethod() {
        return null;
    }
}
