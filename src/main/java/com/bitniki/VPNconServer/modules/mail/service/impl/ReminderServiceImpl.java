package com.bitniki.VPNconServer.modules.mail.service.impl;

import com.bitniki.VPNconServer.modules.mail.entity.ReminderEntity;
import com.bitniki.VPNconServer.modules.mail.exception.ReminderValidationFailedException;
import com.bitniki.VPNconServer.modules.mail.model.ReminderToCreate;
import com.bitniki.VPNconServer.modules.mail.repository.ReminderRepo;
import com.bitniki.VPNconServer.modules.mail.service.ReminderService;
import com.bitniki.VPNconServer.modules.mail.validator.ReminderValidator;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    private ReminderRepo reminderRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<ReminderEntity> getAllUncheckedWhereUserHaveTelegram() {
        // load entities and set checked
        return reminderRepo.findAllWhereUncheckedAndUserWithTelegram().stream()
                .peek(reminder -> reminder.setIsChecked(true))
                .toList();
    }

    @Override
    public ReminderEntity create(@NotNull ReminderToCreate model) throws ReminderValidationFailedException, UserNotFoundException {
        final LocalDate DAY_STAMP = LocalDate.now();

        // validate model
        Validator validator = ReminderValidator.validateAllFields(model);
        if (validator.hasFails()) {
            throw new ReminderValidationFailedException(validator.toString());
        }

        // load user
        UserEntity user = userService.getOneById(model.getUserId());

        // return old if exist else create new
        return reminderRepo.findByUserIdAndReminderTypeAndDayStamp(model.getUserId(), model.getReminderType(), DAY_STAMP)
                .orElseGet(() -> reminderRepo.save(
                        ReminderEntity.builder()
                                .reminderType(model.getReminderType())
                                .isChecked(false)
                                .dayStamp(DAY_STAMP)
                                .user(user)
                                .payload(model.getPayload())
                                .build()
                        )
                );
    }
}
