package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.MailEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MailRepo extends CrudRepository<MailEntity, Long> {
    List<MailEntity> findByUser(UserEntity user);
    List<MailEntity> findByForTelegramAndIsChecked(Boolean forTelegram, Boolean isChecked);
    void deleteByIsChecked(Boolean isChecked);
}
