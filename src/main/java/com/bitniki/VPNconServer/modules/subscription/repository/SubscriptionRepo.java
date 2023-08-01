package com.bitniki.VPNconServer.modules.subscription.repository;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepo extends CrudRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByRole(Role role);
}
