package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.exception.notFoundException.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.model.Subscription;
import com.bitniki.VPNconServer.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subs")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getAll() {
        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<Subscription> getOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> createOne(@RequestBody SubscriptionEntity entity)
            throws SubscriptionValidationFailedException {
        return ResponseEntity.ok(subscriptionService.createSubscription(entity));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> deleteOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(subscriptionService.deleteSubscription(id));
    }
}
