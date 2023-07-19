package com.bitniki.VPNconServer.modules.metacodes.service.impl;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.metacodes.entity.MetacodeEntity;
import com.bitniki.VPNconServer.modules.metacodes.exception.MetacodeNotFoundException;
import com.bitniki.VPNconServer.modules.metacodes.exception.MetacodeValidationException;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToCreate;
import com.bitniki.VPNconServer.modules.metacodes.model.MetacodeToUse;
import com.bitniki.VPNconServer.modules.metacodes.operation.Operation;
import com.bitniki.VPNconServer.modules.metacodes.repository.MetacodeRepo;
import com.bitniki.VPNconServer.modules.metacodes.service.MetacodeService;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.role.Role;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MetacodeServiceImpl implements MetacodeService {

    private final MetacodeRepo metacodeRepo;
    private final UserService userService;

    /**
     * Генерация случайной строки, безопасного для использования в url и длиной до 10 символов.
     * @return Случайная строка для метакода.
     */
    private String genRandomString() {
        final int LEFT_LIMIT = 48; // numeral '0'
        final int RIGHT_LIMIT = 122; // letter 'z'
        final int MAX_SIZE = 10; // max string length
        Random random = new Random();

        return random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                // filtering out non number or non alphabet characters
                .filter(i -> (i <= 57) || (i >= 65 && i <= 90) || (i >= 97))
                .limit(MAX_SIZE)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public MetacodeEntity generateCode(@NotNull MetacodeToCreate metacode) throws MetacodeValidationException {
        // get operation
        Operation operation = metacode.getOperation();
        if (operation == null) {
            throw new MetacodeValidationException("Failed resolve operation");
        }

        // make metacode
        MetacodeEntity metacodeEntity = MetacodeEntity.builder()
                .code(genRandomString())
                .operation(operation)
                .build();

        // save and return
        return metacodeRepo.save(metacodeEntity);
    }

    /**
     * Обновление роли юзера по данной роли.
     * @param login логин юзера.
     * @param newRole Новая роль юзера.
     */
    private void updateRoleForUser(String login, Role newRole) throws UserNotFoundException {
        // find user
        UserEntity entity = userService.getOneByLogin(login);

        userService.updateUserRole(entity.getId(), newRole);
    }

    @Override
    public String resolveMetacodeByLogin(@NotNull String login, @NotNull MetacodeToUse metacode) throws EntityNotFoundException {
        // resolve metacode
        Operation operation = metacodeRepo.findByCode(metacode.getCode())
                .orElseThrow(() -> new MetacodeNotFoundException("Failed to resolve metacode"))
                .getOperation();


        // do operation
        switch (operation) {
            case UPDATE_ROLE_TO_ACTIVATED_CLOSE_USER -> updateRoleForUser(login, Role.ACTIVATED_CLOSE_USER);
            case UPDATE_ROLE_TO_ADMIN -> updateRoleForUser(login, Role.ADMIN);
        }

        return "Success";
    }

}
