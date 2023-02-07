package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.MailEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.MailValidationFailedException;
import com.bitniki.VPNconServer.model.Mail;
import com.bitniki.VPNconServer.repository.MailRepo;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class MailService {
    @Autowired
    private MailRepo mailRepo;
    @Autowired
    private UserRepo userRepo;

    public List<Mail> getAll() {
        List<Mail> mails = new ArrayList<>();
        mailRepo.findAll().forEach(entity -> mails.add(Mail.toModel(entity)));
        return mails;
    }

    public List<Mail> getByForTelegramAndChecked(Boolean forTelegram, Boolean isChecked) {
        List<MailEntity> entities = mailRepo.findByForTelegramAndIsChecked(forTelegram, isChecked);
        //Set checked
        entities.forEach(entity -> entity.setChecked(true));
        return entities.stream().map(Mail::toModel).toList();
    }

    private Mail createMail(UserEntity user, MailEntity mail) {
        mail.setUser(user);
        MailEntity savedMail = mailRepo.save(mail);
        return Mail.toModel(savedMail);
    }

    public Mail createByUserId(Long userId, Long telegramId, String login, MailEntity mail)
            throws UserNotFoundException, MailValidationFailedException {
        //Find user
        UserEntity user;
        if(userId != null) {
            user = userRepo.findById(userId)
                    .orElseThrow(
                            () -> new UserNotFoundException("User with id " + userId + " not found")
                    );
        } else if (telegramId != null) {
            user = userRepo.findByTelegramId(telegramId)
                    .orElseThrow(
                            () -> new UserNotFoundException("User with telegramId " + telegramId + " not found")
                    );
        } else if (login != null) {
            user = userRepo.findByLogin(login);
            if (user == null)
                throw new UserNotFoundException("User with login " + login + " not found");
        } else {
            throw new MailValidationFailedException("No user identification was given");
        }
        return createMail(user, mail);
    }

    public Mail createByUserId(Long userId, MailEntity mail)
            throws UserNotFoundException, MailValidationFailedException {
        //Find user
        UserEntity user;
        if(userId != null) {
            user = userRepo.findById(userId)
                    .orElseThrow(
                            () -> new UserNotFoundException("User with id " + userId + " not found")
                    );
        } else {
            throw new MailValidationFailedException("No user identification was given");
        }
        return createMail(user, mail);
    }

    public void deleteCheckedMails() {
        mailRepo.deleteByIsChecked(true);
    }

    public Mail saveCheque(String cheque) {
        //Find accountant user
        UserEntity accountant = userRepo.findByLogin("accountant");
        //Save Cheque
        MailEntity mail = new MailEntity();
        mail.setPayload(cheque);
        return createMail(accountant, mail);
    }
    public Mail saveCheque(MailEntity cheque) {
        //Find accountant user
        UserEntity accountant = userRepo.findByLogin("accountant");
        //Save Cheque
        return createMail(accountant, cheque);
    }
}
