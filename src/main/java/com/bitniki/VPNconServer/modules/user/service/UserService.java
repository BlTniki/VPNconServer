package com.bitniki.VPNconServer.modules.user.service;


import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.User;

import java.util.List;

@SuppressWarnings("unused")
public interface UserService {
    /**
     * @return Возвращает список всех пользователей.
     */
    List<User> getAll();

    /**
     * Возвращает юзера с данным id.
     * @param id id юзера.
     * @return Юзер с id.
     * @throws UserNotFoundException Если юзер не найден.
     */
    User getOne (Long id) throws UserNotFoundException;

    /**
     * Возвращает юзера с данным логином.
     * @param login логин юзера
     * @return Юзер с данным логином.
     * @throws UserNotFoundException Если юзер не найден.
     */
    User getOne (String login) throws UserNotFoundException;

    /**
     * Возвращает юзера с данным telegramId.
     * @param telegramId id пользователя в telegram.
     * @return юзер с данным telegramId.
     * @throws UserNotFoundException Если юзер не найден.
     */
    User getOneByTelegramId (Long telegramId) throws UserNotFoundException;

    /**
     * Метод создаёт нового пользователя.
     * В теле запроса необходимо указать {@link String} login и {@link String} password.
     * @param user Сущность нового юзера.
     * {@link String} login и {@link String} password необходимо указать. Остальные поля необязательны.
     * @return Нового юзера
     * @throws UserAlreadyExistException Если юзер с данным логином уже существует.
     * @throws UserValidationFailedException Если поля {@link String} login и {@link String} password не прошли валидацию.
     */
    User create (UserEntity user) throws UserAlreadyExistException, UserValidationFailedException;

    /**
     * Обновляет предоставленные поля в существующем юзере с данным id.
     * @param id id Существующего юзера.
     * @param newUser Сущность юзера с указанными полями, которые следует изменить.
     * @return Обновлённого юзера.
     * @throws UserAlreadyExistException Если новый логин уже занят.
     * @throws UserNotFoundException Если юзер с данным id не найден.
     * @throws UserValidationFailedException Если новые поля проваливают валидацию.
     */
    User update (Long id, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;

    /**
     * Обновляет предоставленные поля в существующем юзере с данным логином.
     * @param login логин юзера.
     * @param newUser Сущность юзера с указанными полями, которые следует изменить.
     * @return Обновлённого юзера.
     * @throws UserAlreadyExistException Если новый логин уже занят.
     * @throws UserNotFoundException Если юзер с данным логином не найден.
     * @throws UserValidationFailedException Если новые поля проваливают валидацию.
     */
    User update (String login, UserEntity newUser) throws UserAlreadyExistException, UserNotFoundException, UserValidationFailedException;

    /**
     * Удаляет юзера с данным id.
     * @param id id пользователя.
     * @return удалённого юзера.
     * @throws UserNotFoundException Если юзер с данным id не найден.
     */
    User delete(Long id) throws UserNotFoundException;

    /**
     * удаляет юзера по логину.
     * @param login логин юзера.
     * @return удалённого юзера.
     * @throws UserNotFoundException Если юзер с данным логином не найден.
     */
    User delete(String login) throws UserNotFoundException;
}
