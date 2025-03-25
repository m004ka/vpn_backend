package org.application.prod.repository;

import org.application.prod.models.Payment;
import org.application.prod.models.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> getPaymentsByYookassaPaymentId(String id);

}
