package com.bitniki.VPNconServer.modules.payment.repository;

import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentRepo extends CrudRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByBillId(String billId);
}
