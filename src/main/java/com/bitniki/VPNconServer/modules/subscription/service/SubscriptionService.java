package com.bitniki.VPNconServer.modules.subscription.service;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionAlreadyExistException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.SubscriptionFromRequest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Spliterator;

public interface SubscriptionService {
    Spliterator<SubscriptionEntity> getAll();

    List<SubscriptionEntity> getAllByRole(@NotNull Role role);

    SubscriptionEntity getOneById(@NotNull Long id) throws SubscriptionNotFoundException;
    
    SubscriptionEntity create(@NotNull SubscriptionFromRequest model) throws SubscriptionValidationFailedException;
    
    SubscriptionEntity updateById(@NotNull Long id, @NotNull SubscriptionFromRequest model) throws SubscriptionNotFoundException, SubscriptionAlreadyExistException, SubscriptionValidationFailedException;

    SubscriptionEntity deleteById(@NotNull Long id) throws SubscriptionNotFoundException;
}
