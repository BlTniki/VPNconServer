package com.bitniki.VPNconServer.modules.subscription.service;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.SubscriptionFromRequest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Spliterator;

public interface SubscriptionService {
    /**
     * Возвращает список всех подписок.
     * @return {@link Spliterator}, содержащий объекты {@link SubscriptionEntity}.
     */
    Spliterator<SubscriptionEntity> getAll();

    /**
     * Возвращает список всех подписок с определённой ролью.
     * @param role Роль, по которой выполняется поиск.
     * @return Список {@link SubscriptionEntity}.
     */
    List<SubscriptionEntity> getAllByRole(@NotNull Role role);

    /**
     * Возвращает подписку с данным Id.
     * @param id Id подписки.
     * @return Сущность подписки.
     * @throws SubscriptionNotFoundException Если подписка с данным Id не найдена.
     */
    SubscriptionEntity getOneById(@NotNull Long id) throws SubscriptionNotFoundException;

    /**
     * Создаёт новую подписку.
     * @param model Модель {@link SubscriptionFromRequest} для создания подписки.
     * @return Сущность созданной подписки.
     * @throws SubscriptionValidationFailedException если валидация для модели не прошла успешно.
     */
    SubscriptionEntity create(@NotNull SubscriptionFromRequest model) throws SubscriptionValidationFailedException;

    /**
     * Обновление сущности подписки.
     * @param id Id существующей подписки.
     * @param model Модель {@link SubscriptionFromRequest} для обновления подписки.
     *              Следует указать только те поля, которые необходимо обновить.
     * @return Сущность обновлённой подписки.
     * @throws SubscriptionNotFoundException Если подписка с данным Id не найдена.
     */
    SubscriptionEntity updateById(@NotNull Long id, @NotNull SubscriptionFromRequest model) throws SubscriptionNotFoundException;

    /**
     * Удаление подписки с данным Id.
     * @param id  Id подписки.
     * @return Сущность удалённой подписки.
     * @throws SubscriptionNotFoundException Если подписка с данным Id не найдена.
     */
    SubscriptionEntity deleteById(@NotNull Long id) throws SubscriptionNotFoundException;
}
