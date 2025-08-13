package com.skillforge.skillforge_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayCallbackRequest {
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;

    // Convert from request parameters
    public static VNPayCallbackRequest fromRequestParams(Map<String, String> params) {
        VNPayCallbackRequest callback = new VNPayCallbackRequest();
        callback.setVnp_Amount(params.get("vnp_Amount"));
        callback.setVnp_BankCode(params.get("vnp_BankCode"));
        callback.setVnp_BankTranNo(params.get("vnp_BankTranNo"));
        callback.setVnp_CardType(params.get("vnp_CardType"));
        callback.setVnp_OrderInfo(params.get("vnp_OrderInfo"));
        callback.setVnp_PayDate(params.get("vnp_PayDate"));
        callback.setVnp_ResponseCode(params.get("vnp_ResponseCode"));
        callback.setVnp_TmnCode(params.get("vnp_TmnCode"));
        callback.setVnp_TransactionNo(params.get("vnp_TransactionNo"));
        callback.setVnp_TransactionStatus(params.get("vnp_TransactionStatus"));
        callback.setVnp_TxnRef(params.get("vnp_TxnRef"));
        callback.setVnp_SecureHash(params.get("vnp_SecureHash"));
        return callback;
    }
}
