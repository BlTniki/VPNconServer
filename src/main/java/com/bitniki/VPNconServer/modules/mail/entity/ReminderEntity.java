package com.bitniki.VPNconServer.modules.mail.entity;

import com.bitniki.VPNconServer.modules.mail.type.ReminderType;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reminders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReminderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReminderType reminderType;

    @Column(nullable = false)
    private Boolean isChecked;

    @Column(nullable = false)
    private LocalDate dayStamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String payload;
}
