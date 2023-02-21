package com.bitniki.VPNconServer.modules.mail.controller;

import com.bitniki.VPNconServer.modules.mail.model.Mail;
import com.bitniki.VPNconServer.modules.mail.entity.MailEntity;
import com.bitniki.VPNconServer.modules.mail.service.MailService;
import com.bitniki.VPNconServer.modules.mail.exception.MailValidationFailedException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('mail:read')")
    public ResponseEntity<List<Mail>> getAll() {
        return ResponseEntity.ok(mailService.getAll());
    }
    @GetMapping("/with")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('mail:read')")
    public ResponseEntity<List<Mail>> getSpecified(@RequestParam Boolean forTelegram,
                                                   @RequestParam Boolean isChecked) {
        return ResponseEntity.ok(mailService.getByForTelegramAndChecked(forTelegram,isChecked));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('mail:write')")
    public ResponseEntity<Mail> createOne (
            @RequestParam Long userId,
            @RequestParam Long telegramId,
            @RequestParam String login,
            @RequestBody MailEntity mail
            ) throws UserNotFoundException, MailValidationFailedException {
        return ResponseEntity.ok(mailService.createByUserId(userId,telegramId,login,mail));
    }

    @DeleteMapping("/checked")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('mail:write')")
    public ResponseEntity<Boolean> deleteChecked() {
        mailService.deleteCheckedMails();
        return ResponseEntity.ok(true);
    }

    @PostMapping("/accountant")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('mail:write')")
    public ResponseEntity<Mail> saveCheque(@RequestBody MailEntity mail) {
        return ResponseEntity.ok(mailService.saveCheque(mail));
    }
}
