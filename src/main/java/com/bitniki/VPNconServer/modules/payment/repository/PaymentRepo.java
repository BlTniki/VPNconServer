package com.bitniki.VPNconServer.modules.payment.repository;

import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import com.bitniki.VPNconServer.modules.payment.status.PaymentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface PaymentRepo extends CrudRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByUuid(String uuid);

    @Query("SELECT p.uuid FROM PaymentEntity p WHERE p.status = :status AND p.time_stamp >= :timeStamp")
    Set<String> findUuidsByStatusAndMinTimestamp(PaymentStatus status, LocalDateTime timeStamp);
}
