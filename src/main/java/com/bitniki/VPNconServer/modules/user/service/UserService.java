package com.bitniki.VPNconServer.modules.user.service;


import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;

public interface UserService {
    /**
     * @return Возвращает список всех пользователей.
     */
    Spliterator<UserEntity> getAll();

    /**
     * Возвращает юзера с данным id.
     * @param id id юзера.
     * @return Юзер с id.
     * @throws UserNotFoundException Если юзер не найден.
     */
    UserEntity getOneById(@NotNull Long id) throws UserNotFoundException;

    /**
     * Возвращает юзера с данным логином.
     * @param login логин юзера
     * @return Юзер с данным логином.
     * @throws UserNotFoundException Если юзер не найден.
     */
    UserEntity getOneByLogin(@NotNull String login) throws UserNotFoundException;

    /**
     * Возвращает юзера с данным telegramId.
     * @param telegramId id пользователя в telegram.
     * @return юзер с данным telegramId.
     * @throws UserNotFoundException Если юзер не найден.
     */
    UserEntity getOneByTelegramId (@NotNull Long telegramId) throws UserNotFoundException;

    /**
     * Метод создаёт нового пользователя.
     * @param model Сущность нового юзера. Правила для полей можно найти в {@link UserFromRequest}.
     * {@link String} login и {@link String} password необходимо указать.
     * @return Нового юзера
     * @throws UserAlreadyExistException Если юзер с данным логином уже существует.
     * @throws UserValidationFailedException Если поля {@link String} login и {@link String} password не прошли валидацию.
     */
    UserEntity create (@NotNull UserFromRequest model) throws UserAlreadyExistException, UserValidationFailedException;

    /**
     * Обновляет предоставленные поля в существующем юзере с данным id.
     * @param id id Существующего юзера.
     * @param newUser Сущность юзера с указанными полями, которые следует изменить. Правила для полей можно найти в {@link UserFromRequest}.
     * @return Обновлённого юзера.
     * @throws UserAlreadyExistException Если новый логин уже занят.
     * @throws UserNotFoundException Если юзер с данным id не найден.
     * @throws UserValidationFailedException Если новые поля проваливают валидацию.
     */
    UserEntity updateById(@NotNull Long id, @NotNull UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;

    /**
     * Обновляет предоставленные поля в существующем юзере с данным логином.
     * @param login логин юзера.
     * @param newUser Сущность юзера с указанными полями, которые следует изменить. Правила для полей можно найти в {@link UserFromRequest}.
     * @return Обновлённого юзера.
     * @throws UserAlreadyExistException Если новый логин уже занят.
     * @throws UserNotFoundException Если юзер с данным логином не найден.
     * @throws UserValidationFailedException Если новые поля проваливают валидацию.
     */
    UserEntity updateByLogin(@NotNull String login, @NotNull UserFromRequest newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;

    /**
     * Удаляет юзера с данным id.
     * @param id id пользователя.
     * @return удалённого юзера.
     * @throws UserNotFoundException Если юзер с данным id не найден.
     */
    UserEntity deleteById(@NotNull Long id) throws UserNotFoundException;

    /**
     * удаляет юзера по логину.
     * @param login логин юзера.
     * @return удалённого юзера.
     * @throws UserNotFoundException Если юзер с данным логином не найден.
     */
    UserEntity deleteByLogin(@NotNull String login) throws UserNotFoundException;
}
