package com.bitniki.VPNconServer.modules.mail.model;

import com.bitniki.VPNconServer.modules.mail.type.ReminderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReminderToCreate {
    private ReminderType reminderType;
    private Long userId;
    private String payload;
}
