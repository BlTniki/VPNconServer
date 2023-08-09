package com.bitniki.VPNconServer.modules.mail.model;

import com.bitniki.VPNconServer.modules.mail.entity.ReminderEntity;
import com.bitniki.VPNconServer.modules.mail.type.ReminderType;
import com.bitniki.VPNconServer.modules.user.model.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reminder {
    private Long id;
    private ReminderType reminderType;
    private User user;
    private String payload;

    public static Reminder toModel(ReminderEntity entity) {
        return Reminder.builder()
                .id(entity.getId())
                .reminderType(entity.getReminderType())
                .user(User.toModel(entity.getUser()))
                .payload(entity.getPayload())
                .build();
    }
}
