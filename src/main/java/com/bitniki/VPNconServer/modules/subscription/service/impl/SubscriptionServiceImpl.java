package com.bitniki.VPNconServer.modules.subscription.service.impl;


import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionAlreadyExistException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.SubscriptionFromRequest;
import com.bitniki.VPNconServer.modules.subscription.repository.SubscriptionRepo;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import com.bitniki.VPNconServer.modules.subscription.validator.SubscriptionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Override
    public Spliterator<SubscriptionEntity> getAll() {
        return subscriptionRepo.findAll().spliterator();
    }

    @Override
    public List<SubscriptionEntity> getAllByRole(@NotNull Role role) {
        return subscriptionRepo.findByRole(role);
    }

    @Override
    public SubscriptionEntity getOneById(@NotNull Long id) throws SubscriptionNotFoundException {
        return subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription with id %d not found".formatted(id))
                );
    }

    @Override
    public SubscriptionEntity create(@NotNull SubscriptionFromRequest model) throws SubscriptionValidationFailedException {
        // valid entity
        SubscriptionValidator validator = SubscriptionValidator.validateAllFields(model);
        if(validator.hasFails()) {
            throw new SubscriptionValidationFailedException(validator.toString());
        }

        // create entity
        SubscriptionEntity entity = SubscriptionEntity.builder()
                .role(model.getRole())
                .priceInRub(model.getPriceInRub())
                .peersAvailable(model.getPeersAvailable())
                .durationDays(model.getDurationDays())
                .build();

        return subscriptionRepo.save(entity);
    }

    @Override
    public SubscriptionEntity updateById(@NotNull Long id, @NotNull SubscriptionFromRequest model)
            throws SubscriptionNotFoundException{
        // load entity
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription with id %d not found".formatted(id))
                );

        // update entity
        entity.setRole(model.getRole() == null ? entity.getRole() : model.getRole());
        entity.setPriceInRub(model.getPriceInRub() == null ? entity.getPriceInRub() : model.getPriceInRub());
        entity.setPeersAvailable(model.getPeersAvailable() == null ? entity.getPeersAvailable() : model.getPeersAvailable());
        entity.setDurationDays(model.getDurationDays() == null ? entity.getDurationDays() : model.getDurationDays());

        return subscriptionRepo.save(entity);
    }

    @Override
    public SubscriptionEntity deleteById(@NotNull Long id) throws SubscriptionNotFoundException {
        // load entity
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription with id %d not found".formatted(id))
                );

        // delete entity
        subscriptionRepo.delete(entity);

        return entity;
    }
}
