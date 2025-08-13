package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Payment;
import com.skillforge.skillforge_api.utils.constant.PaymentMethod;
import com.skillforge.skillforge_api.utils.constant.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByStatusAndCreatedAtBetween(PaymentStatus status, LocalDateTime start, LocalDateTime end);
    List<Payment> findByPaymentMethodOrderByCreatedAtDesc(PaymentMethod paymentMethod);
}
