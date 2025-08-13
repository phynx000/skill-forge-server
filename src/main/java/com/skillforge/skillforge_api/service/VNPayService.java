package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.config.VNPayConfig;
import com.skillforge.skillforge_api.dto.request.VNPayCallbackRequest;
import com.skillforge.skillforge_api.dto.request.VNPayRequest;
import com.skillforge.skillforge_api.dto.response.VNPayResponse;
import com.skillforge.skillforge_api.repository.PaymentRepository;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class VNPayService {
    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;

    public VNPayService(VNPayConfig vnPayConfig, PaymentRepository paymentRepository) {
        this.vnPayConfig = vnPayConfig;
        this.paymentRepository = paymentRepository;
    }

    public VNPayResponse createPaymentUrl(VNPayRequest request, HttpServletRequest httpRequest) {
        try {
            String vnp_TxnRef = request.getTransactionId() != null ?
                    request.getTransactionId() : VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(httpRequest);

            // Convert amount to VNPay format (multiply by 100)
            long amount = request.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnPayConfig.getVersion());
            vnp_Params.put("vnp_Command", vnPayConfig.getCommand());
            vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            if (request.getBankCode() != null && !request.getBankCode().isEmpty()) {
                vnp_Params.put("vnp_BankCode", request.getBankCode());
            }

            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo() != null ?
                    request.getOrderInfo() : "Payment for order " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", request.getOrderType() != null ?
                    request.getOrderType() : vnPayConfig.getOrderType());

            String locale = request.getLanguage();
            if (locale != null && !locale.isEmpty()) {
                vnp_Params.put("vnp_Locale", locale);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }

            vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Create date
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Build query string and hash data
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();

            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnPayConfig.getPayUrl() + "?" + queryUrl;

            log.info("Created VNPay payment URL for transaction: {}", vnp_TxnRef);

            return VNPayResponse.builder()
                    .code("00")
                    .message("success")
                    .paymentUrl(paymentUrl)
                    .transactionId(vnp_TxnRef)
                    .build();

        } catch (Exception e) {
            log.error("Error creating VNPay payment URL", e);
            return VNPayResponse.builder()
                    .code("99")
                    .message("Error creating payment URL: " + e.getMessage())
                    .build();
        }
    }

    public boolean verifyPaymentCallback(Map<String, String> params) {
        try {
            String vnp_SecureHash = params.get("vnp_SecureHash");
            params.remove("vnp_SecureHashType");
            params.remove("vnp_SecureHash");

            // Build hash data
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();

            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }

            String signValue = VNPayConfig.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
            return signValue.equals(vnp_SecureHash);

        } catch (Exception e) {
            log.error("Error verifying VNPay callback", e);
            return false;
        }
    }

    public PaymentStatus processCallback(VNPayCallbackRequest callback) {
        if ("00".equals(callback.getVnp_ResponseCode()) &&
                "00".equals(callback.getVnp_TransactionStatus())) {
            return PaymentStatus.SUCCESS;
        } else {
            return PaymentStatus.FAILED;
        }
    }


}
