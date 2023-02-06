package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.model.Role;
import com.bitniki.VPNconServer.model.Subscription;
import com.bitniki.VPNconServer.model.UserWithRelations;
import com.bitniki.VPNconServer.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
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

    @GetMapping("/byRole/{role}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(subscriptionService.getByRole(role));
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

    @PostMapping("/manage")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<UserWithRelations> addToUser(@RequestParam Long subs_id, @RequestParam Long user_id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(subscriptionService.addSubscriptionToUser(subs_id, user_id));
    }

    @DeleteMapping("/manage")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<UserWithRelations> delFromUser(@RequestParam Long user_id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(subscriptionService.deleteSubscriptionFromUser(user_id));
    }
}
