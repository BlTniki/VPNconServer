package com.bitniki.VPNconServer.modules.mail.controller;

import com.bitniki.VPNconServer.modules.mail.exception.ReminderValidationFailedException;
import com.bitniki.VPNconServer.modules.mail.model.Reminder;
import com.bitniki.VPNconServer.modules.mail.model.ReminderToCreate;
import com.bitniki.VPNconServer.modules.mail.service.ReminderService;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
    @Autowired
    private ReminderService reminderService;

    /**
     * Возвращает список всех непроверенных уведомлений, у которых юзер привязал телеграмм.
     * @return Список {@link Reminder}.
     */
    @GetMapping("/tg")
    @PreAuthorize("hasAuthority('any') && hasAuthority('reminder:read')")
    public ResponseEntity<List<Reminder>> getAllUncheckedWithTelegramm() {
        return ResponseEntity.ok(
                reminderService.getAllUncheckedWhereUserHaveTelegram().stream()
                        .map(Reminder::toModel)
                        .toList()
        );
    }

    /**
     * Создаёт новое уведомление или возвращает старое,
     * если определённая комбинация полей уже встречалась в БД.
     * @param model Поля, необходимые для создания.
     * @return Новое уведомление или уже существующее с такими же полями.
     * @throws UserNotFoundException Если указанный юзер не существует.
     * @throws ReminderValidationFailedException Если тело реквеста не прошло валидацию
     */
    @PostMapping
    @PreAuthorize("hasAuthority('any') && hasAuthority('reminder:write')")
    public ResponseEntity<Reminder> create(@RequestBody ReminderToCreate model)
            throws UserNotFoundException, ReminderValidationFailedException {
        return ResponseEntity.ok(
                Reminder.toModel(reminderService.create(model))
        );
    }
}
