package com.skillforge.skillforge_api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class JsonConvertor {

    private final ObjectMapper objectMapper;

    public JsonConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convertToJson(PaymentRequestDTO request) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            ObjectNode paymentNode = objectMapper.createObjectNode();

            // Basic payment information
            paymentNode.put("paymentMethod", request.getPaymentMethod().name());
            paymentNode.put("amount", request.getAmount());
            paymentNode.put("currency", request.getCurrency());
            paymentNode.put("createdTime", LocalDateTime.now().toString());

            // E-wallet specific information
            if (request.getEWalletInfo() != null) {
                ObjectNode ewalletNode = objectMapper.createObjectNode();
                ewalletNode.put("walletProvider", request.getEWalletInfo().getWalletProvider());
                ewalletNode.put("phoneNumber", maskPhoneNumber(request.getEWalletInfo().getPhoneNumber()));

                if (request.getEWalletInfo().getWalletId() != null) {
                    ewalletNode.put("walletId", request.getEWalletInfo().getWalletId());
                }

                paymentNode.set("eWalletInfo", ewalletNode);
            }

            // Debit card specific information (if applicable)
            if (request.getDebitCardInfo() != null) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("cardHolderName", request.getDebitCardInfo().getCardHolderName());
                cardNode.put("maskedCardNumber", maskCardNumber(request.getDebitCardInfo().getCardNumber()));
                cardNode.put("expiryDate", request.getDebitCardInfo().getExpiryDate());
                // Never store CVV for security

                paymentNode.set("debitCardInfo", cardNode);
            }

            return objectMapper.writeValueAsString(paymentNode);

        } catch (Exception e) {
            log.error("Error converting PaymentRequest to JSON", e);
            return "{}";
        }
    }

    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return phoneNumber;
        }

        String prefix = phoneNumber.substring(0, 3);
        String suffix = phoneNumber.substring(phoneNumber.length() - 2);
        String masked = "*".repeat(phoneNumber.length() - 5);

        return prefix + masked + suffix;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }

        String prefix = cardNumber.substring(0, 4);
        String suffix = cardNumber.substring(cardNumber.length() - 4);
        String masked = "*".repeat(cardNumber.length() - 8);

        return prefix + masked + suffix;
    }


}
