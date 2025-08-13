package com.skillforge.skillforge_api.utils.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.request.VNPayRequest;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.dto.response.VNPayResponse;
import com.skillforge.skillforge_api.entity.Payment;
import com.skillforge.skillforge_api.repository.PaymentRepository;
import com.skillforge.skillforge_api.service.PaymentService;
import com.skillforge.skillforge_api.service.VNPayService;
import com.skillforge.skillforge_api.utils.JsonConvertor;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Slf4j
public class VNPayEWalletPaymentStrategy implements PaymentStrategy {

    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final JsonConvertor jsonConvertor;

    public VNPayEWalletPaymentStrategy(VNPayService vnPayService,
                                       PaymentRepository paymentRepository,
                                      JsonConvertor jsonConvertor) {
        this.vnPayService = vnPayService;
        this.paymentRepository = paymentRepository;

        this.jsonConvertor = jsonConvertor;
    }

    @Override
    public PaymentDTO processPayment(PaymentRequestDTO request) {
        log.info("Processing VNPay e-wallet payment for amount: {}", request.getAmount());

        try {
            validateEWalletInfo(request.getEWalletInfo());

            String transactionId = generateTransactionId();

            // Create VNPay request
            VNPayRequest vnPayRequest = VNPayRequest.builder()
                    .amount(request.getAmount())
                    .orderInfo("E-wallet payment via " + request.getEWalletInfo().getWalletProvider())
                    .orderType("billpayment")
                    .language("vn")
                    .transactionId(transactionId)
                    .currency(request.getCurrency())
                    .build();

            // Get current HTTP request
            HttpServletRequest httpRequest = getCurrentHttpRequest();

            VNPayResponse vnPayResponse = vnPayService.createPaymentUrl(vnPayRequest, httpRequest);

            if ("00".equals(vnPayResponse.getCode())) {
                // Save pending payment
                savePendingPayment(request, transactionId);

                return PaymentDTO.builder()
                        .transactionId(transactionId)
                        .status(PaymentStatus.PENDING)
                        .paymentMethod(PaymentMethod.E_WALLET)
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .createdAt(LocalDateTime.now())
                        .message("Redirect to VNPay for payment")
                        .paymentUrl(vnPayResponse.getPaymentUrl())
                        .build();
            } else {
                throw new RuntimeException("VNPay payment URL creation failed: " + vnPayResponse.getMessage());
            }

        } catch (Exception e) {
            log.error("VNPay e-wallet payment failed", e);
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

    private void validateEWalletInfo(PaymentRequestDTO.EWalletInfo walletInfo) {
        if (walletInfo == null) {
            throw new IllegalArgumentException("E-wallet information is required");
        }

        // Validate that wallet provider is VNPay supported
        String provider = walletInfo.getWalletProvider().toUpperCase();
        if (!Arrays.asList("VNPAY", "MOMO", "ZALOPAY", "VIETINBANK", "VIETCOMBANK").contains(provider)) {
            throw new IllegalArgumentException("Unsupported wallet provider for VNPay: " + provider);
        }
    }

    private void savePendingPayment(PaymentRequestDTO request, String transactionId) {
        try {
            Payment payment = Payment.builder()
                    .transactionId(transactionId)
                    .paymentMethod(PaymentMethod.E_WALLET)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(PaymentStatus.PENDING)
                    .paymentDetails(jsonConvertor.convertToJson(request))
                    .build();

            paymentRepository.save(payment);
            log.info("Pending payment saved with transaction ID: {}", transactionId);
        } catch (Exception e) {
            log.error("Failed to save pending payment record", e);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        throw new RuntimeException("Not in a web request context");
    }

//    private String convertToJson(PaymentRequestDTO request) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//            ObjectNode paymentNode = objectMapper.createObjectNode();
//
//            // Basic payment information
//            paymentNode.put("paymentMethod", request.getPaymentMethod().name());
//            paymentNode.put("amount", request.getAmount());
//            paymentNode.put("currency", request.getCurrency());
//            paymentNode.put("createdTime", LocalDateTime.now().toString());
//
//            // E-wallet specific information
//            if (request.getEWalletInfo() != null) {
//                ObjectNode ewalletNode = objectMapper.createObjectNode();
//                ewalletNode.put("walletProvider", request.getEWalletInfo().getWalletProvider());
//                ewalletNode.put("phoneNumber", maskPhoneNumber(request.getEWalletInfo().getPhoneNumber()));
//
//                if (request.getEWalletInfo().getWalletId() != null) {
//                    ewalletNode.put("walletId", request.getEWalletInfo().getWalletId());
//                }
//
//                paymentNode.set("eWalletInfo", ewalletNode);
//            }
//
//            // Debit card specific information (if applicable)
//            if (request.getDebitCardInfo() != null) {
//                ObjectNode cardNode = objectMapper.createObjectNode();
//                cardNode.put("cardHolderName", request.getDebitCardInfo().getCardHolderName());
//                cardNode.put("maskedCardNumber", maskCardNumber(request.getDebitCardInfo().getCardNumber()));
//                cardNode.put("expiryDate", request.getDebitCardInfo().getExpiryDate());
//                // Never store CVV
//
//                paymentNode.set("debitCardInfo", cardNode);
//            }
//
//            return objectMapper.writeValueAsString(paymentNode);
//
//        } catch (Exception e) {
//            log.error("Error converting PaymentRequest to JSON", e);
//            return "{}";
//        }
//    }


    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.E_WALLET;
    }

    private String generateTransactionId() {
        return "VNPAY_" + System.currentTimeMillis();
    }


}
