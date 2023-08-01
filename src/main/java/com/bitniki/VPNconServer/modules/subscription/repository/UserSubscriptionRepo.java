package com.bitniki.VPNconServer.modules.subscription.repository;

import com.bitniki.VPNconServer.modules.subscription.entity.UserSubscriptionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepo extends CrudRepository<UserSubscriptionEntity, Long> {
    Optional<UserSubscriptionEntity> findByUserId(Long userId);
    List<UserSubscriptionEntity> findBySubscriptionId(Long subscriptionId);
}
