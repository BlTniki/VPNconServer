package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.exception.notFoundException.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.model.Subscription;
import com.bitniki.VPNconServer.repository.SubscriptionRepo;
import com.bitniki.VPNconServer.validator.SubscriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    public List<Subscription> getAll() {
        List<SubscriptionEntity> subscriptions = new ArrayList<>();
        subscriptionRepo.findAll().forEach(subscriptions::add);
        return subscriptions.stream().map(Subscription::toModel).toList();
    }

    public Subscription getById(Long id) throws SubscriptionNotFoundException {
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );
        return Subscription.toModel(entity);
    }

    public Subscription createSubscription(SubscriptionEntity entity) throws SubscriptionValidationFailedException {
        //valid entity
        SubscriptionValidator validator = SubscriptionValidator.validateAllFields(entity);
        if(validator.hasFails()) {
            throw new SubscriptionValidationFailedException(validator.toString());
        }
        //save and return
        return Subscription.toModel(subscriptionRepo.save(entity));
    }

    public Subscription deleteSubscription(Long id) throws SubscriptionNotFoundException {
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );
        subscriptionRepo.delete(entity);
        return Subscription.toModel(entity);
    }
}
