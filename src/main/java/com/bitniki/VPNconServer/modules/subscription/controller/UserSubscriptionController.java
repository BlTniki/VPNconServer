package com.bitniki.VPNconServer.modules.subscription.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.UserSubscription;
import com.bitniki.VPNconServer.modules.subscription.model.UserSubscriptionFromRequest;
import com.bitniki.VPNconServer.modules.subscription.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/userSubs")
public class UserSubscriptionController {
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    /**
     * Получает список всех подписок пользователей.
     *
     * @return Ответ со списком подписок пользователей
     */
    @GetMapping
    @PreAuthorize("hasAuthority('any') && hasAuthority('user_subscription:read')")
    public ResponseEntity<List<UserSubscription>> getAll() {
        return ResponseEntity.ok(
                StreamSupport.stream(userSubscriptionService.getAll(), false)
                        .map(UserSubscription::toModel)
                        .toList()
        );
    }

    /**
     * Возвращает список ассоциаций юзеров и подписок по дню сгорания подписки.
     * @param expirationDay день сгорания подписки.
     * @return Ответ со списком подписок пользователей.
     */
    @GetMapping("/byExpirationDay/{expirationDay}")
    @PreAuthorize("hasAuthority('any') && hasAuthority('user_subscription:read')")
    public ResponseEntity<List<UserSubscription>> getAllByExpirationDay(LocalDate expirationDay) {
        return ResponseEntity.ok(
                userSubscriptionService.getAllByExpirationDay(expirationDay).stream()
                        .map(UserSubscription::toModel)
                        .toList()
        );
    }

    /**
     * Получает информацию о подписке пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Ответ с информацией о подписке пользователя
     * @throws UserSubscriptionNotFoundException если подписка пользователя не найдена
     */
    // TODO: На этой задачи лежит технический долг. Проверь записи.
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('any') && hasAuthority('user_subscription:read')")
    public ResponseEntity<UserSubscription> getOneByUserId(@PathVariable Long userId)
            throws UserSubscriptionNotFoundException {
        return ResponseEntity.ok(
                UserSubscription.toModel(userSubscriptionService.getOneByUserId(userId))
        );
    }

    /**
     * Добавляет подписку пользователю.
     *
     * @param model Данные для добавления подписки пользователю
     * @return Ответ с информацией о добавленной подписке пользователю
     * @throws UserSubscriptionValidationFailedException если данные некорректны для создания подписки
     * @throws EntityNotFoundException если пользователь или подписка не найдены
     */
    @PostMapping
    @PreAuthorize("hasAuthority('any') && hasAuthority('user_subscription:write')")
    public ResponseEntity<UserSubscription> addSubscriptionToUser(@RequestBody UserSubscriptionFromRequest model)
            throws UserSubscriptionValidationFailedException, EntityNotFoundException {
        return ResponseEntity.ok(
                UserSubscription.toModel(userSubscriptionService.addSubscriptionToUser(model))
        );
    }

    /**
     * Удаляет подписку пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Ответ с информацией об удаленной подписке пользователя
     * @throws UserSubscriptionNotFoundException если подписка пользователя не найдена
     */
    @DeleteMapping("/userId")
    @PreAuthorize("hasAuthority('any') && hasAuthority('user_subscription:write')")
    public ResponseEntity<UserSubscription> deleteByUserId(@PathVariable Long userId)
            throws UserSubscriptionNotFoundException {
        return ResponseEntity.ok(
                UserSubscription.toModel(userSubscriptionService.deleteByUserId(userId))
        );
    }
}
