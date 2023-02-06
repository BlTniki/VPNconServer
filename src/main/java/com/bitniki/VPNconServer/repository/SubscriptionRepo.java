package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepo extends CrudRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByRole(Role role);
}
