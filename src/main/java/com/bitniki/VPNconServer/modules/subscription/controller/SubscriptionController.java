package com.bitniki.VPNconServer.modules.subscription.controller;

import com.bitniki.VPNconServer.modules.role.Role;
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

    /**
     * Получает список всех подписок.
     *
     * @return Ответ со списком подписок
     */
    @GetMapping
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getAll() {
        return ResponseEntity.ok(
                StreamSupport.stream(subscriptionService.getAll(), false)
                        .map(Subscription::toModel)
                        .toList()
        );
    }

    /**
     * Получает список подписок по указанной роли.
     *
     * @param role Роль, для которой ищутся подписки
     * @return Ответ со списком подписок для указанной роли
     */
    @GetMapping("/byRole/{role}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<List<Subscription>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(
                subscriptionService.getAllByRole(role).stream()
                        .map(Subscription::toModel)
                        .toList()
        );
    }

    /**
     * Получает информацию о подписке по указанному идентификатору.
     *
     * @param id Идентификатор подписки
     * @return Ответ с информацией о подписке
     * @throws SubscriptionNotFoundException если подписка не найдена
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:read')")
    public ResponseEntity<Subscription> getOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.getOneById(id))
        );
    }

    /**
     * Создает новую подписку.
     *
     * @param model Данные для создания подписки
     * @return Ответ с информацией о созданной подписке
     * @throws SubscriptionValidationFailedException если данные недостаточны или некорректны для создания подписки
     */
    @PostMapping
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> create(@RequestBody SubscriptionFromRequest model)
            throws SubscriptionValidationFailedException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.create(model))
        );
    }

    /**
     * Обновляет информацию о существующей подписке.
     *
     * @param id    Идентификатор подписки, которую необходимо обновить
     * @param model Новые данные для подписки
     * @return Ответ с обновленной информацией о подписке
     * @throws SubscriptionNotFoundException         если подписка не найдена
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> update(@PathVariable Long id, @RequestBody SubscriptionFromRequest model)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.updateById(id, model))
        );
    }

    /**
     * Удаляет подписку по указанному идентификатору.
     *
     * @param id Идентификатор подписки, которую необходимо удалить
     * @return Ответ с информацией об удаленной подписке
     * @throws SubscriptionNotFoundException если подписка не найдена
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subscription:write')")
    public ResponseEntity<Subscription> deleteOne(@PathVariable Long id)
            throws SubscriptionNotFoundException {
        return ResponseEntity.ok(
                Subscription.toModel(subscriptionService.deleteById(id))
        );
    }
}
