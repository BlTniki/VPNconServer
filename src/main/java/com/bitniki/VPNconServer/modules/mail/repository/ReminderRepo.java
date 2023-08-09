package com.bitniki.VPNconServer.modules.mail.repository;

import com.bitniki.VPNconServer.modules.mail.entity.ReminderEntity;
import com.bitniki.VPNconServer.modules.mail.type.ReminderType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReminderRepo extends CrudRepository<ReminderEntity, Long> {
    Optional<ReminderEntity> findByUserIdAndReminderTypeAndDayStamp(Long userId, ReminderType reminderType, LocalDate dayStamp);

    @Query("SELECT r FROM ReminderEntity r RIGHT OUTER JOIN UserEntity u ON u.id = r.user.id WHERE r.isChecked = false AND u.telegramId IS NOT NULL")
    List<ReminderEntity> findAllWhereUncheckedAndUserWithTelegram();
}
