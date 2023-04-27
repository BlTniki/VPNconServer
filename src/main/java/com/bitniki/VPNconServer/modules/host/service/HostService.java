package com.bitniki.VPNconServer.modules.host.service;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.host.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.modules.host.exception.HostNotFoundException;
import com.bitniki.VPNconServer.modules.host.exception.HostValidationFailedException;
import com.bitniki.VPNconServer.modules.host.model.Host;

import java.util.List;

public interface HostService {

    /**
     * Получает список всех хостов.
     * @return список объектов типа Host.
     */
    List<Host> getAll ();

    /**
     * Получает хост по его идентификатору.
     * @param id идентификатор хоста.
     * @return объект типа Host.
     * @throws HostNotFoundException если хост не найден.
     */
    Host getOne (Long id) throws HostNotFoundException;

    /**
     * Создает новый хост на основе переданных данных.
     * @param host HostEntity. Должны быть указанны все поля.
     * @return объект типа Host.
     * @throws HostAlreadyExistException если хост с данными именем или ip уже существует.
     * @throws HostValidationFailedException если данные хоста не прошли валидацию.
     */
    Host create (HostEntity host) throws HostAlreadyExistException, HostValidationFailedException;

    /**
     * Обновляет данные существующего хоста.
     * @param id идентификатор хоста.
     * @param newHost HostEntity, с ненулевыми полями, которые следует изменить.
     * @return объект типа Host.
     * @throws HostNotFoundException если хост не найден.
     * @throws HostAlreadyExistException если хост с данными именем или ip уже существует.
     * @throws HostValidationFailedException если данные хоста не прошли валидацию.
     */
    Host update (Long id, HostEntity newHost) throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException;

    /**
     * Удаляет хост по его идентификатору.
     * @param id идентификатор хоста.
     * @return объект типа Host, который был удален.
     * @throws HostNotFoundException если хост не найден.
     */
    Host delete (Long id) throws HostNotFoundException;
}
