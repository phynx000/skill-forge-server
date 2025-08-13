package com.skillforge.skillforge_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillforge.skillforge_api.dto.request.PaymentRequestDTO;
import com.skillforge.skillforge_api.dto.request.VNPayCallbackRequest;
import com.skillforge.skillforge_api.dto.response.PaymentDTO;
import com.skillforge.skillforge_api.entity.Payment;
import com.skillforge.skillforge_api.repository.PaymentRepository;
import com.skillforge.skillforge_api.utils.JsonConvertor;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.strategy.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentService {
    private final Map<PaymentMethod, PaymentStrategy> paymentStrategies;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final JsonConvertor jsonConvertor;

    public PaymentService(List<PaymentStrategy> strategies,
                          PaymentRepository paymentRepository,
                          ObjectMapper objectMapper, JsonConvertor jsonConvertor) {
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
        this.paymentStrategies = strategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getSupportedMethod,
                        strategy -> strategy
                ));
        this.jsonConvertor = jsonConvertor;
    }

    public PaymentDTO processPayment(PaymentRequestDTO request) {
        log.info("Processing payment with method: {}", request.getPaymentMethod());

        PaymentStrategy strategy = paymentStrategies.get(request.getPaymentMethod());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + request.getPaymentMethod());
        }

        // Process payment
        PaymentDTO response = strategy.processPayment(request);

        // Save to database
        savePaymentRecord(request, response);

        return response;
    }

    private void savePaymentRecord(PaymentRequestDTO request, PaymentDTO response) {
        try {
            Payment payment = Payment.builder()
                    .transactionId(response.getTransactionId())
                    .paymentMethod(request.getPaymentMethod())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(response.getStatus())
                    .paymentDetails(jsonConvertor.convertToJson(request))
                    .build();

            paymentRepository.save(payment);
            log.info("Payment record saved with transaction ID: {}", response.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to save payment record", e);
        }
    }


    public List<PaymentMethod> getSupportedPaymentMethods() {
        return new ArrayList<>(paymentStrategies.keySet());
    }

    public String updatePaymentDetailsWithCallback(String currentDetails, VNPayCallbackRequest callback) {
        try {
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

            // Add response code meaning
            vnpayInfo.put("responseMessage", getVNPayResponseMessage(callback.getVnp_ResponseCode()));

            // Add or update vnpay section
            detailsNode.set("vnpayCallback", vnpayInfo);

            return objectMapper.writeValueAsString(detailsNode);

        } catch (Exception e) {
            log.error("Error updating payment details with VNPay callback", e);
            // Return original details if update fails
            return currentDetails;
        }
    }



    private String getVNPayResponseMessage(String responseCode) {
        switch (responseCode) {
            case "00": return "Giao dịch thành công";
            case "07": return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10": return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11": return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12": return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13": return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP).";
            case "24": return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51": return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65": return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75": return "Ngân hàng thanh toán đang bảo trì.";
            case "79": return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định.";
            default: return "Lỗi không xác định: " + responseCode;
        }
    }



}
