package com.bitniki.VPNconServer.modules.metacodes.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.metacodes.entity.MetacodeEntity;
import com.bitniki.VPNconServer.modules.metacodes.exception.MetacodeValidationException;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToCreate;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToUse;
import org.jetbrains.annotations.NotNull;

/**
 * Сервис для генерации, а также валидации кодов, для которых требуется выполнить действие.
 * Данный сервис предназначен для выполнения действий по коду.
 */
public interface MetacodeService {

    /**
     * Генерация метакода по данной операции.
     * @param metacode Сущность, содержащая операцию, для которой следует создать метакод.
     * @return Сущность метакода, содержащая метакод и операцию по этому метакоду.
     * @throws MetacodeValidationException В случае неизвестной операции.
     */
    MetacodeEntity generateCode(@NotNull MetacodeToCreate metacode) throws MetacodeValidationException;

    /**
     * Поиск метакода в базе и выполнение операции юзеру, которое ассоциировано с метакодом.
     * @param login Логин юзера, к которому следует применить операцию.
     * @param metacode Метакод, с которым ассоциирована операция.
     * @return Строку "Success".
     * @throws EntityNotFoundException Если метакод или юзер не найден.
     */
    String resolveMetacodeByLogin(@NotNull String login, @NotNull MetacodeToUse metacode) throws EntityNotFoundException;
}
