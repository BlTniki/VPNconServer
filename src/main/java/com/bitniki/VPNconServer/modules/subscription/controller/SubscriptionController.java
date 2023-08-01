package com.bitniki.VPNconServer.modules.subscription.controller;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionAlreadyExistException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.Subscription;
import com.bitniki.VPNconServer.modules.subscription.model.SubscriptionFromRequest;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/subs")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getAll() {
        return ResponseEntity.ok(
                StreamSupport.stream(subscriptionService.getAll(), false)
                        .map(Subscription::toModel)
                        .toList()
        );
    }

    @GetMapping("/byRole/{role}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(
                subscriptionService.getAllByRole(role).stream()
                        .map(Subscription::toModel)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<Subscription> getOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.getOneById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> create(@RequestBody SubscriptionFromRequest model)
            throws SubscriptionValidationFailedException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.create(model))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> update(@PathVariable Long id, @RequestBody SubscriptionFromRequest model)
            throws SubscriptionValidationFailedException, SubscriptionAlreadyExistException, SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.updateById(id, model))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> deleteOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.deleteById(id))
        );
    }
}
