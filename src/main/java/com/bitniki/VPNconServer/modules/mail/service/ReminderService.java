package com.bitniki.VPNconServer.modules.mail.service;

import com.bitniki.VPNconServer.modules.mail.entity.ReminderEntity;
import com.bitniki.VPNconServer.modules.mail.exception.ReminderValidationFailedException;
import com.bitniki.VPNconServer.modules.mail.model.ReminderToCreate;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ReminderService {
    /**
     * Получение всех непроверенных писем, юзера которых имеют телеграм Id.
     * @return Список с напоминаниями.
     */
    List<ReminderEntity> getAllUncheckedWhereUserHaveTelegram();

    /**
     * Создаёт напоминание для юзера об оплате, если в этот день ещё не было создано.
     * @param model Необходимые поля для создания.
     * @return Новую сущность или уже существующую.
     * @throws ReminderValidationFailedException Если поля для создания не прошли валидацию
     */
    ReminderEntity create(@NotNull ReminderToCreate model) throws ReminderValidationFailedException, UserNotFoundException;
}
