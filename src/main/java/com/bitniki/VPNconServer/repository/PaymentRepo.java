package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentRepo extends CrudRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByBillId(String billId);
}
