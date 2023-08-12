package com.bitniki.VPNconServer.modules.host.service;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.HostFromRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Spliterator;

public interface HostService {

    /**
     * Получает список всех хостов.
     *
     * @return список объектов типа Host.
     */
    Spliterator<HostEntity> getAll ();

    /**
     * Получает хост по его идентификатору.
     * @param id идентификатор хоста.
     * @return объект типа Host.
     * @throws HostNotFoundException если хост не найден.
     */
    HostEntity getOneById(@NotNull Long id) throws HostNotFoundException;

    /**
     * Создает новый хост на основе переданных данных.
     * @param host HostFromRequest. Должны быть указанны все поля.
     * @return объект типа Host.
     * @throws HostAlreadyExistException если хост с данными именем или парой ip и порта уже существует.
     * @throws HostValidationFailedException если данные хоста не прошли валидацию.
     */
    HostEntity create (@NotNull HostFromRequest host) throws HostAlreadyExistException, HostValidationFailedException;

    /**
     * Обновляет данные существующего хоста.
     * @param id идентификатор хоста.
     * @param newHost HostFromRequest, с ненулевыми полями, которые следует изменить.
     * @return объект типа Host.
     * @throws HostNotFoundException если хост не найден.
     * @throws HostAlreadyExistException если хост с данными именем или парой ip и порта уже существует.
     * @throws HostValidationFailedException если данные хоста не прошли валидацию.
     */
    HostEntity updateById(@NotNull Long id, @NotNull HostFromRequest newHost) throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException;

    /**
     * Удаляет хост по его идентификатору.
     * @param id идентификатор хоста.
     * @return объект типа Host, который был удален.
     * @throws HostNotFoundException если хост не найден.
     */
    HostEntity deleteById(@NotNull Long id) throws HostNotFoundException;

    /**
     * Подсчёт кол-ва доступных пиров на хосте.
     * @param id Id хоста.
     * @return Число доступных пиров.
     * @throws HostNotFoundException Если хост не найден.
     */
    Integer countAvailablePeersOnHost(@NotNull Long id) throws HostNotFoundException;

    /**
     * Метод для получения паттернов валидации полей {@link String} ipaddress и {@link String} networkPrefix.
     * @return Карту в виде {"ipaddress": pattern, "networkPrefix": pattern}.
     */
    Map<String, String> getValidationRegex();
}
