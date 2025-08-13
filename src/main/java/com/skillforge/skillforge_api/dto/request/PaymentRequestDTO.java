package com.skillforge.skillforge_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    // Thông tin cho thẻ ghi nợ
    @JsonProperty("debitCardInfo")
    private DebitCardInfo debitCardInfo;
    // Thông tin cho ví điện tử
    @JsonProperty("eWalletInfo")
    private EWalletInfo eWalletInfo;



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebitCardInfo {
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        private String cardNumber;

        @NotBlank(message = "Card holder name is required")
        private String cardHolderName;

        @NotBlank(message = "Expiry date is required")
        @Pattern(regexp = "\\d{2}/\\d{2}", message = "Expiry date format should be MM/YY")
        private String expiryDate;

        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "\\d{3}", message = "CVV must be 3 digits")
        private String cvv;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EWalletInfo {
        @NotBlank(message = "Wallet provider is required")
        private String walletProvider; // MOMO, ZALOPAY, VNPAY, etc.

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^(\\+84|0)[3-9]\\d{8}$", message = "Invalid phone number format")
        private String phoneNumber;

        private String walletId;
    }
}
