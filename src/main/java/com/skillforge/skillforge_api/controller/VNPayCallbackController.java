package com.skillforge.skillforge_api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.skillforge.skillforge_api.dto.request.VNPayCallbackRequest;
import com.skillforge.skillforge_api.entity.Payment;
import com.skillforge.skillforge_api.repository.PaymentRepository;
import com.skillforge.skillforge_api.service.PaymentService;
import com.skillforge.skillforge_api.service.VNPayService;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payments/vnpay")
@Slf4j
public class VNPayCallbackController {

    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public VNPayCallbackController(VNPayService vnPayService, PaymentRepository paymentRepository, PaymentService paymentService) {
        this.vnPayService = vnPayService;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }


    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }

        log.info("Received VNPay callback with params: {}", params);

        try {
            // Verify callback signature
            boolean isValid = vnPayService.verifyPaymentCallback(params);
            if (!isValid) {
                log.error("Invalid VNPay callback signature");
                return ResponseEntity.badRequest().body("Invalid signature");
            }

            VNPayCallbackRequest callback = VNPayCallbackRequest.fromRequestParams(params);
            PaymentStatus status = vnPayService.processCallback(callback);

            // Update payment status in database
            updatePaymentStatus(callback.getVnp_TxnRef(), status, callback);

            if (status == PaymentStatus.SUCCESS) {
                log.info("Payment successful for transaction: {}", callback.getVnp_TxnRef());
                return ResponseEntity.ok("Payment successful");
            } else {
                log.warn("Payment failed for transaction: {}", callback.getVnp_TxnRef());
                return ResponseEntity.ok("Payment failed");
            }

        } catch (Exception e) {
            log.error("Error processing VNPay callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/ipn")
    public ResponseEntity<Map<String, String>> handleIPN(HttpServletRequest request) {
        // Instant Payment Notification - for server-to-server callback
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }

        log.info("Received VNPay IPN with params: {}", params);

        Map<String, String> response = new HashMap<>();

        try {
            boolean isValid = vnPayService.verifyPaymentCallback(params);
            if (!isValid) {
                response.put("RspCode", "97");
                response.put("Message", "Invalid signature");
                return ResponseEntity.ok(response);
            }

            VNPayCallbackRequest callback = VNPayCallbackRequest.fromRequestParams(params);

            // Check if order exists
            Optional<Payment> existingPayment = paymentRepository.findByTransactionId(callback.getVnp_TxnRef());
            if (!existingPayment.isPresent()) {
                response.put("RspCode", "01");
                response.put("Message", "Order not found");
                return ResponseEntity.ok(response);
            }

            Payment payment = existingPayment.get();

            // Check amount
            long vnpAmount = Long.parseLong(callback.getVnp_Amount());
            long orderAmount = payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            if (vnpAmount != orderAmount) {
                response.put("RspCode", "04");
                response.put("Message", "Invalid amount");
                return ResponseEntity.ok(response);
            }

            // Update payment status
            PaymentStatus status = vnPayService.processCallback(callback);
            updatePaymentStatus(callback.getVnp_TxnRef(), status, callback);

            response.put("RspCode", "00");
            response.put("Message", "Confirm success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing VNPay IPN", e);
            response.put("RspCode", "99");
            response.put("Message", "Unknown error");
            return ResponseEntity.ok(response);
        }
    }

    private void updatePaymentStatus(String transactionId, PaymentStatus status, VNPayCallbackRequest callback) {
        try {
            Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(transactionId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setStatus(status);

                // Update payment details with VNPay response
                String updatedDetails = paymentService.updatePaymentDetailsWithCallback(
                        payment.getPaymentDetails(), callback);
                payment.setPaymentDetails(updatedDetails);

                paymentRepository.save(payment);
                log.info("Updated payment status to {} for transaction: {}", status, transactionId);
            } else {
                log.warn("Payment not found for transaction: {}", transactionId);
            }
        } catch (Exception e) {
            log.error("Error updating payment status", e);
        }
    }

    private String updatePaymentDetails(String currentDetails, VNPayCallbackRequest callback) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode currentNode;

            // Parse current details or create empty object
            if (currentDetails != null && !currentDetails.trim().isEmpty()) {
                currentNode = objectMapper.readTree(currentDetails);
            } else {
                currentNode = objectMapper.createObjectNode();
            }

            // Convert to ObjectNode for modification
            ObjectNode detailsNode = (ObjectNode) currentNode;

            // Add VNPay callback information
            ObjectNode vnpayInfo = objectMapper.createObjectNode();
            vnpayInfo.put("vnp_Amount", callback.getVnp_Amount());
            vnpayInfo.put("vnp_BankCode", callback.getVnp_BankCode());
            vnpayInfo.put("vnp_BankTranNo", callback.getVnp_BankTranNo());
            vnpayInfo.put("vnp_CardType", callback.getVnp_CardType());
            vnpayInfo.put("vnp_OrderInfo", callback.getVnp_OrderInfo());
            vnpayInfo.put("vnp_PayDate", callback.getVnp_PayDate());
            vnpayInfo.put("vnp_ResponseCode", callback.getVnp_ResponseCode());
            vnpayInfo.put("vnp_TmnCode", callback.getVnp_TmnCode());
            vnpayInfo.put("vnp_TransactionNo", callback.getVnp_TransactionNo());
            vnpayInfo.put("vnp_TransactionStatus", callback.getVnp_TransactionStatus());
            vnpayInfo.put("vnp_TxnRef", callback.getVnp_TxnRef());
            vnpayInfo.put("callbackTime", LocalDateTime.now().toString());

            // Add or update vnpay section
            detailsNode.set("vnpayCallback", vnpayInfo);

            return objectMapper.writeValueAsString(detailsNode);

        } catch (Exception e) {
            log.error("Error updating payment details with VNPay callback", e);
            // Return original details if update fails
            return currentDetails;
        }
    }
}
