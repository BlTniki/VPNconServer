package com.bitniki.VPNconServer.modules.mail.repository;

import com.bitniki.VPNconServer.modules.mail.entity.MailEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MailRepo extends CrudRepository<MailEntity, Long> {
    List<MailEntity> findByUser(UserEntity user);
    List<MailEntity> findByForTelegramAndIsChecked(Boolean forTelegram, Boolean isChecked);
    void deleteByIsChecked(Boolean isChecked);
}
