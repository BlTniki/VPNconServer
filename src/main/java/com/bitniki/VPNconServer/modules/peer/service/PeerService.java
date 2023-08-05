package com.bitniki.VPNconServer.modules.peer.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;

public interface PeerService {
    /**
     * @return Итератор, содержащий объекты {@link PeerEntity}
     */
    Spliterator<PeerEntity> getAll();

    /**
     * Возвращает список всех объектов PeerEntity, у которых юзер с указанным Id.
     *
     * @param userId Id {@link UserEntity} для поиска объектов PeerEntity
     * @return Список, содержащий объекты PeerEntity
     */
    List<PeerEntity> getAllByUserId(@NotNull Long userId);

    /**
     * Возвращает итератор для получения всех объектов PeerEntity, у которых юзер с указанным логином.
     *
     * @param login Логин {@link UserEntity} для поиска объектов PeerEntity
     * @return Итератор, содержащий объекты PeerEntity
     */
    Spliterator<PeerEntity> getAllByLogin(@NotNull String login);

    /**
     * Возвращает объект PeerEntity по заданному идентификатору.
     *
     * @param id Идентификатор объекта PeerEntity
     * @return Объект PeerEntity
     * @throws PeerNotFoundException Если объект не найден по указанному идентификатору
     */
    PeerEntity getOneById(@NotNull Long id) throws PeerNotFoundException;

    /**
     * Возвращает объект PeerEntity, связанный с указанным логином {@link UserEntity} и идентификатором {@link PeerEntity}.
     *
     * @param login Логин {@link UserEntity}
     * @param id    Идентификатор объекта PeerEntity
     * @return Объект PeerEntity
     * @throws EntityNotFoundException Если объект не найден по указанным логину и идентификатору
     */
    PeerEntity getOneByLoginAndPeerId(@NotNull String login, @NotNull Long id) throws EntityNotFoundException;

    /**
     * Создает новый объект PeerEntity на основе переданной модели {@link PeerFromRequest}.
     * Необходимые поля в {@link PeerFromRequest} можно посмотреть внутри класса.
     *
     * @param model Модель PeerFromRequest для создания объекта PeerEntity.
     * @return Созданный объект PeerEntity.
     * @throws EntityValidationFailedException При провале проверки полей в {@link PeerFromRequest}.
     * @throws PeerAlreadyExistException Если пир с комбинацией peerConfName + userId + hostId или peerIp + hostId уже существует.
     * @throws EntityNotFoundException Если данные userId и/или hostId не найдены.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    PeerEntity create(@NotNull PeerFromRequest model)
            throws EntityValidationFailedException, PeerAlreadyExistException, EntityNotFoundException, PeerConnectHandlerException;

    /**
     * Создает новый объект PeerEntity на основе логина юзера переданной модели {@link PeerFromRequest}.
     * Необходимые поля в {@link PeerFromRequest} можно посмотреть внутри класса.
     *
     * @param login логин юзера {@link UserEntity}.
     * @param model Модель PeerFromRequest для создания объекта PeerEntity.
     * @return Созданный объект PeerEntity.
     * @throws EntityValidationFailedException При провале проверки полей в {@link PeerFromRequest} или переданный логин не совпадает с логином юзера в {@link PeerFromRequest}.
     * @throws PeerAlreadyExistException Если пир с комбинацией peerConfName + userId + hostId или peerIp + hostId уже существует.
     * @throws EntityNotFoundException Если данные userId и/или hostId не найдены.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    PeerEntity createByLogin(@NotNull String login, @NotNull PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException;

    /**
     * Удаляет объект PeerEntity по указанному идентификатору.
     * @param id id Идентификатор объекта PeerEntity.
     * @return Удаленный объект PeerEntity.
     * @throws PeerNotFoundException Если объект не найден по указанному идентификатору.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    PeerEntity delete(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;

    /**
     * Удаляет объект PeerEntity, связанный с указанным логином {@link UserEntity} и идентификатором.
     * @param login логин юзера {@link UserEntity}.
     * @param id id id Идентификатор объекта PeerEntity.
     * @return Удаленный объект PeerEntity.
     * @throws EntityNotFoundException Если объект не найден по указанному логином {@link UserEntity} и идентификатором.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    PeerEntity deleteByLogin(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException;

    /**
     * Возвращает токен для скачивания конфига с хоста с указанным идентификатором PeerEntity.
     *
     * @param id Идентификатор объекта PeerEntity
     * @return Токен для скачивания файла
     * @throws PeerNotFoundException Если объект не найден по указанному идентификатору
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    String getDownloadTokenForPeerById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;

    /**
     * Возвращает токен для скачивания конфига с хоста с указанным логином {@link UserEntity} и идентификатором PeerEntity.
     *
     * @param login логин юзера {@link UserEntity}.
     * @param id Идентификатор объекта PeerEntity.
     * @return Токен для скачивания файла.
     * @throws PeerNotFoundException Если объект не найден по указанной паре логина и идентификатора.
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    String getDownloadTokenForPeerByLoginAndId(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException;

    /**
     * Активирует PeerEntity на хосте с указанным идентификатором.
     *
     * @param id Идентификатор объекта PeerEntity
     * @return True, если активация прошла успешно, иначе False
     * @throws PeerNotFoundException Если объект не найден по указанному идентификатору
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    Boolean activatePeerOnHostById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;

    /**
     * Деактивирует PeerEntity на хосте с указанным идентификатором.
     *
     * @param id Идентификатор объекта PeerEntity
     * @return True, если деактивация прошла успешно, иначе False
     * @throws PeerNotFoundException Если объект не найден по указанному идентификатору
     * @throws PeerConnectHandlerException Если возникли проблемы на стороне хоста.
     */
    Boolean deactivatePeerOnHostById(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;

    /**
     * Метод для получения паттернов валидации полей {@link String} peerIp и {@link String} peerConfName.
     * @return Карту в виде {"peerIp": pattern, "peerConfName": pattern}.
     */
    Map<String, String> getValidationRegex();
}