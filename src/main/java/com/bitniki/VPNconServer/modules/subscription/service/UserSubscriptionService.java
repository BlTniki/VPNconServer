package com.bitniki.VPNconServer.modules.subscription.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.entity.UserSubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.UserSubscriptionFromRequest;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Spliterator;

/**
 * Этот класс служит сервисом для работы со связями между юзерами и подписками.
 * Далее эти связи будут называться ассоциациями юзера и подписки или просто подпиской юзера.
 */
public interface UserSubscriptionService {
    /**
     * Возвращает список всех ассоциаций юзеров и подписок.
     * @return {@link Spliterator}, содержащий объекты {@link UserSubscriptionEntity}.
     */
    Spliterator<UserSubscriptionEntity> getAll();

    /**
     * Получение сущности ассоциации юзера м подписки по Id юзера.
     * @param userId Id юзера.
     * @return Сущности ассоциации юзера м подписки
     * @throws UserSubscriptionNotFoundException Если для юзера с данным Id не найдена подписка.
     */
    UserSubscriptionEntity getOneByUserId(@NotNull Long userId) throws UserSubscriptionNotFoundException;

    /**
     * Получения всех подписок юзера по дню сгорания подписки.
     * @param day День сгорания подписки.
     * @return Список подписок юзеров.
     */
    List<UserSubscriptionEntity> getAllByExpirationDay(@NotNull LocalDate day);

    /**
     * Добавление подписки юзеру.
     * Если у юзера нет актуальной подписки, то день сгорания вычисляется как сегодняшняя дата плюс дни действия подписки.
     * Если у юзера есть актуальная подписка и добавляется та жа подписка, то день сгорания вычисляется как дата сгорания подписки плюс действия подписки.
     * Если у юзера есть актуальная подписка и добавляется другая подписка, то день сгорания вычисляется основываясь на кол-ве дней, что остались по старой подписке.
     * @param model Модель {@link UserSubscriptionFromRequest} для добавления юзеру подписки.
     * @return Сущность ассоциации юзера и подписки.
     * @throws UserSubscriptionValidationFailedException Если модель {@link UserSubscriptionFromRequest} не прошла валидацию.
     * @throws EntityNotFoundException Если юзер или подписка не найдена.
     */
    UserSubscriptionEntity addSubscriptionToUser(@NotNull UserSubscriptionFromRequest model) throws UserSubscriptionValidationFailedException, EntityNotFoundException;

    /**
     * Удаление ассоциации юзера и подписки.
     * @param userId Id юзера.
     * @return Сущность удалённой ассоциации юзера и подписки.
     * @throws UserSubscriptionNotFoundException Если ассоциация между юзером и подписки по-данному Id юзера не найдена.
     */
    UserSubscriptionEntity deleteByUserId(@NotNull Long userId) throws UserSubscriptionNotFoundException;

    /**
     * Проверка может ли юзер создать ещё одного пира.
     * @param userId Id юзера.
     * @param userExistingPeersNumber число уже существующих пиров у юзера.
     * @return Значение True, если юзер может создать ещё одного пира.
     */
    Boolean isUserCanCreatePeer(@NotNull Long userId, int userExistingPeersNumber);
}
